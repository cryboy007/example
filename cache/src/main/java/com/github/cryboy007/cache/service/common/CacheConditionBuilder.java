package com.github.cryboy007.cache.service.common;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.alibaba.fastjson.util.TypeUtils;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.github.cryboy007.exception.BizCode;
import com.github.cryboy007.exception.BizException;
import com.github.cryboy007.utils.CommonConvertUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.joor.Reflect;
import org.springframework.util.StopWatch;

/**
 * <p>
 * 查询条件构造器
 * </p>
 *
 * @author lei.mao
 * @since 2021/5/21
 */
@Slf4j
public class CacheConditionBuilder<R, T extends CommonQuery> {
    private static final String REGEX = ",|，|\\s+";
    private static final String SQL_FORMAT = "SELECT 1 FROM %s WHERE %s = %s %s";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    private R r;

    private List<T> cacheData;

    private Class<T> entityClass;

    private Set<Condition> conditions;

    private LambdaQueryChainWrapper<T> queryChainWrapper;

    public R getR() {
        return r;
    }

    public CacheConditionBuilder(List<T> cacheData, R r) {
        this.r = r;
        this.cacheData = cacheData;
    }

    public CacheConditionBuilder(List<T> cacheData, Class<T> entityClass) {
        this.cacheData = cacheData;
        this.entityClass = entityClass;
    }

    public CacheConditionBuilder(List<T> cacheData, Class<T> entityClass, Set<Condition> conditions) {
        this.cacheData = cacheData;
        this.entityClass = entityClass;
        this.conditions = conditions;
    }
    public CacheConditionBuilder(List<T> cacheData, Class<T> entityClass, Set<Condition> conditions,LambdaQueryChainWrapper<T> lambdaQueryChainWrapper) {
        this.cacheData = cacheData;
        this.entityClass = entityClass;
        this.conditions = conditions;
        this.queryChainWrapper = lambdaQueryChainWrapper;
    }


    public <V> CacheConditionBuilder<R, T> eq(Function<R, V> getValFun, Predicate<T> condition) {
        Optional.ofNullable(this.r).map(getValFun).ifPresent(queryVal -> {
            log.info("比较的值:{}", queryVal);
        });
        this.cacheData = this.cacheData.stream().filter(condition).collect(Collectors.toList());
        return this;
    }

    public List<T> build() {
        if (Objects.isNull(this.cacheData)) {
            throw new BizException(BizCode.CACHE_INIT_ERROR);
        }
        setCondition();
        return this.cacheData;
    }

    public List<T> buildList() {
       /* if (Objects.isNull(this.r)) {
            throw new BizException(BizCode.INVALID_ARGS);
        }*/
        return this.cacheData;
    }



    private String[] getSingleLikeMultiIn(String s) {
        return s.split(REGEX);
    }

    public <C> C buildOne(Class<C> clazz) {
        return CommonConvertUtil.convertTo(this.buildOne(), clazz);
    }

    public T buildOne() {
        if (cacheData != null && cacheData.size() > 1) {
            throw new BizException(BizCode.MULTIPLE_RECORDS);
        }
        return this.cacheData.stream().findFirst().get();
    }

    public T getById(Long id) {
        this.cacheData = this.cacheData.stream().filter(item -> {
            Long value = (Long) Reflect.on(item).field("id").get();
            return id.equals(value);
        }).collect(Collectors.toList());
        return Optional.ofNullable(this.cacheData.get(0)).orElse(null);
    }

    public void setCondition() {
        StopWatch stopWatch = new StopWatch();
        List<Condition> orderBy =
                conditions.stream().filter(item -> item.getType().equals(SqlKeyword.ASC) || item.getType().equals(SqlKeyword.DESC)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(conditions)) {
            conditions.removeAll(orderBy);
            stopWatch.start("buildCondition");
            List<Predicate<T>> predicates = this.buildCondition();
            stopWatch.stop();
            if (!CollectionUtils.isEmpty(predicates)) {
                Predicate<T> predicate = predicates.stream().reduce(Predicate::and).get();
                stopWatch.start("stream-filter");
                this.cacheData = this.cacheData.stream().filter(predicate).collect(Collectors.toList());
                stopWatch.stop();
            }
        }
        //分页
        stopWatch.start("cache limit page start ...");
        Page<Object> localPage = PageHelper.getLocalPage();
        if (localPage != null) {
            try {
                this.cacheData = this.cacheData.stream().skip((localPage.getPageNum() - 1) * localPage.getPageSize())
                        .limit(localPage.getPageSize()).collect(Collectors.toList());
            }finally {
                PageHelper.clearPage();
            }
        }
        stopWatch.stop();
        //排序
        stopWatch.start("cache sort start ...");
        Comparator<T> comparator = buildOrderBy(orderBy);
        if (comparator != null) {
            this.cacheData = this.cacheData.stream().sorted(comparator).collect(Collectors.toList());
        }
        stopWatch.stop();
        log.debug(stopWatch.prettyPrint());
    }

    private Comparator<T> buildOrderBy(List<Condition> orderBy) {
        AtomicReference<Comparator<T>> comparator = new AtomicReference<>();
        orderBy.forEach(condition -> {
                switch (condition.getType()) {
                    case ASC : {
                        String column = StringUtils.sqlInjectionReplaceBlank(condition.getColumn().getColumn());
                        E3Function fun = condition.getColumn();
                        if (comparator.get() == null) {
                            comparator.set(Comparator.comparing(fun));
                        }else {
                            comparator.set(comparator.get().thenComparing(fun));
                        }
                        break;
                    }
                    case DESC:{
                        String column = StringUtils.sqlInjectionReplaceBlank(condition.getColumn().getColumn());
                        E3Function fun = condition.getColumn();
                        if (comparator.get() == null) {
                            comparator.set(Comparator.comparing(fun).reversed());
                        }else {
                            comparator.set(comparator.get().thenComparing(fun).reversed());
                        }
                    }
                }
        });
        return comparator.get();
    }
    private List<Predicate<T>> buildCondition() {
        List<Predicate<T>> listPredicate = new ArrayList<>();
        conditions.forEach(condition -> {
            switch (condition.getType()) {
                case EQ: {
                    String column = condition.getColumn().getColumn();
                    String value = Optional.ofNullable(condition.getValue()).orElse("").toString();
                    //使用接口的方式,不适用反射
                    //String boValue = Optional.ofNullable(Reflect.on(bo).field(column).get()).orElse("").toString();
                    listPredicate.add(t -> t.getValue(t,column).equals(value));
                    break;
                }
                case IN: {
                    String column = condition.getColumn().getColumn();
                    List<String> qtyValue = (ArrayList<String>)Optional.ofNullable(condition.getValue()).orElse(new ArrayList<>());
                    listPredicate.add(t -> qtyValue.contains(t.getValue(t,column)));
                    break;
                }
                case NOT_IN: {
                    String column = condition.getColumn().getColumn();
                    List<String> qtyValue = (ArrayList<String>)Optional.ofNullable(condition.getValue()).orElse(new ArrayList<>());
                    listPredicate.add(t -> qtyValue.contains(t.getValue(t,column)));
                    break;
                }
                case GE:
                case LE:
                case GT:
                case LT:
                    throw new BizException("99999","暂不支持缓存查询中使用表达式" + condition.getType() + "，请联系ht增加功能");
                case LIKE: {
                    String value = Optional.ofNullable(condition.getValue()).orElse("").toString();
                    String column = condition.getColumn().getColumn();
                    String defaultValue = SqlUtils.concatLike(value, SqlLike.DEFAULT);
                    // 所有正则特殊符号进行转义 ^$*.|()\
                    String qtyValue = defaultValue.replaceAll("[\\^$*+?.|()\\\\]", "[$0]")
                            .replaceAll("%", ".*")
                            .replaceAll("_", ".");
                    Pattern pattern = Pattern.compile(qtyValue);
                    listPredicate.add(t -> pattern.matcher(t.getValue(t,column)).matches());
                    break;
                }
                case NOT_LIKE: {
                    String value = Optional.ofNullable(condition.getValue()).orElse("").toString();
                    String column = condition.getColumn().getColumn();
                    String defaultValue = SqlUtils.concatLike(value, SqlLike.DEFAULT);
                    String qtyValue = defaultValue.replaceAll("%", ".*").replaceAll("_", ".");
                    Pattern pattern = Pattern.compile(qtyValue);
                    listPredicate.add(t -> !pattern.matcher(t.getValue(t,column)).matches());
                    break;
                }
                case BETWEEN: {
                    if (handleBetweenCond(condition) != null) {
                        listPredicate.add(handleBetweenCond(condition));
                    }
                }
            }
        });
        return listPredicate;
    }
    //todo 后续需要支持 Number类型和String
    private Predicate<T> handleBetweenCond(Condition condition) {
        return dateBetween(condition);
    }

    private Predicate<T> dateBetween(Condition condition) {
        String column = condition.getColumn().getColumn();
        Object startValue = condition.getStartValue();
        Object endValue = condition.getEndValue();
        Date startDate = TypeUtils.castToDate(startValue);
        Date endDate = TypeUtils.castToDate(endValue);
        if (startDate == null || endDate == null) {
            return null;
        }
        return t -> t.getDate(t,column).compareTo(startDate) >= 0 && t.getDate(t,column).compareTo(endDate) <= 0;
    }


    public <V> CacheConditionBuilder<R, T> eq(Function<R, V> getValFun, E3Function<T, V> field) {
        Optional.ofNullable(this.r).map(getValFun).ifPresent(queryVal -> this.conditions.add(new Condition(SqlKeyword.EQ, field, queryVal)));
        return this;
    }
}


