package com.github.cryboy007.cache.service.common;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.cryboy007.exception.BizCode;
import com.github.cryboy007.exception.BizException;
import com.github.cryboy007.utils.CommonConvertUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.joor.Reflect;

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
        if (CollectionUtils.isNotEmpty(conditions)) {
            /*this.cacheData = this.cacheData.stream().filter(bo -> {
                AtomicBoolean flag = new AtomicBoolean(true);
                buildCondition(bo, flag);
                return flag.get();
            }).collect(Collectors.toList());*/
            List<Predicate<T>> predicates = this.buildCondition();
            if (!CollectionUtils.isEmpty(predicates)) {
                Predicate<T> predicate = predicates.stream().reduce(Predicate::and).get();
                this.cacheData = this.cacheData.stream().filter(predicate).collect(Collectors.toList());
            }
        }
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
                    //String boValue = Optional.ofNullable(Reflect.on(bo).field(column).get()).orElse("").toString();
                    listPredicate.add(t -> qtyValue.contains(t.getValue(t,column)));
                    break;
                }
            }
        });
        return listPredicate;
    }


    public <V> CacheConditionBuilder<R, T> eq(Function<R, V> getValFun, E3Function<T, V> field) {
        Optional.ofNullable(this.r).map(getValFun).ifPresent(queryVal -> this.conditions.add(new Condition(SqlKeyword.EQ, field, queryVal)));
        return this;
    }
}


