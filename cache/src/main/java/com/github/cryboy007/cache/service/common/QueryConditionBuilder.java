package com.github.cryboy007.cache.service.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.github.cryboy007.exception.BizCode;
import com.github.cryboy007.exception.BizException;
import com.github.cryboy007.utils.CollectionUtil;
import com.github.cryboy007.utils.CommonConvertUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <p>
 * 查询条件构造器
 * </p>
 *
 * @author lei.mao
 * @since 2021/5/21
 */
public class QueryConditionBuilder<R, T> {
    private static final String REGEX = ",|，|\\s+";
    private static final String SQL_FORMAT = "SELECT 1 FROM %s WHERE %s = %s %s";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LambdaQueryWrapper<T> queryWrapper;
    private LambdaQueryChainWrapper<T> queryChainWrapper;
    private R r;
    private final Set<Condition> conditions;

    public QueryConditionBuilder(BaseMapper<T> baseMapper) {
        this.queryChainWrapper = new LambdaQueryChainWrapper<>(baseMapper);
        this.queryWrapper = getLambdaQueryWrapper();
        this.conditions = Sets.newConcurrentHashSet();
    }

    public QueryConditionBuilder(BaseMapper<T> baseMapper, LambdaQueryWrapper<T> queryWrapper, R r) {
        this.queryChainWrapper = new LambdaQueryChainWrapper<>(baseMapper);
        this.queryWrapper = queryWrapper;
        this.r = r;
        this.conditions = Sets.newConcurrentHashSet();
    }

    public QueryConditionBuilder(LambdaQueryChainWrapper<T> queryChainWrapper, R r) {
        this.queryChainWrapper = queryChainWrapper;
        this.queryWrapper = getLambdaQueryWrapper();
        this.r = r;
        this.conditions = Sets.newConcurrentHashSet();
    }

    public QueryConditionBuilder(R r) {
        this.r = r;
        this.conditions = Sets.newConcurrentHashSet();
    }

    public R getR() {
        return r;
    }

    public Set<Condition> getConditions() {return this.conditions;}

    public LambdaQueryWrapper<T> getLambdaQueryWrapper() {
        return (LambdaQueryWrapper) this.build().getWrapper();
    }

    public LambdaQueryChainWrapper<T> build() {
        if (Objects.isNull(this.queryChainWrapper)) {
            throw new BizException(BizCode.INVALID_ARGS);
        }

        setCondition();
        return this.queryChainWrapper;
    }

    public T buildOne() {
        if (Objects.isNull(this.queryWrapper)) {
            throw new BizException(BizCode.INVALID_ARGS);
        }
        return this.build().getBaseMapper().selectOne(this.queryWrapper);
    }

    public <C> C buildOne(Class<C> clazz) {
        return CommonConvertUtil.convertTo(this.buildOne(), clazz);
    }

    public List<T> buildList() {
        if (Objects.isNull(this.queryWrapper)) {
            throw new BizException(BizCode.INVALID_ARGS);
        }
        return this.build().getBaseMapper().selectList(this.queryWrapper);
    }

    public <C> List<C> buildList(Class<C> clazz) {
        return CommonConvertUtil.convertToList(this.buildList(), clazz);
    }

    public <C> PageInfo<C> buildPageInfo(int pageNum, int pageSize, Class<C> clazz) {
        PageHelper.startPage(Optional.of(pageNum).orElse(0), Optional.of(pageSize).orElse(10));
        return CommonConvertUtil.convertToPageInfo(this.buildList(), clazz);
    }


    public <V> QueryConditionBuilder<R, T> orderByDesc(SFunction<T, V> getOrderField) {
        this.queryChainWrapper.orderByDesc(getOrderField);
        return this;
    }

    public <V> QueryConditionBuilder<R, T> orderByDesc(SFunction<T, V>... getOrderFields) {
        this.queryChainWrapper.orderByDesc(Arrays.asList(getOrderFields));
        return this;
    }

    public <V> QueryConditionBuilder<R, T> orderByAsc(SFunction<T, V> getOrderField) {
        this.queryChainWrapper.orderByAsc(getOrderField);
        return this;
    }

    public <V> QueryConditionBuilder<R, T> orderByAsc(SFunction<T, V>... getOrderFields) {
        this.queryChainWrapper.orderByAsc(Arrays.asList(getOrderFields));
        return this;
    }

    public <V> QueryConditionBuilder<R, T> eq(Function<R, V> getValFun, E3Function<T, V> getQueryField) {
        Optional.ofNullable(this.r).map(getValFun).ifPresent(queryVal -> this.conditions.add(new Condition(SqlKeyword.EQ, getQueryField, queryVal)));
        return this;
    }

    public <V> QueryConditionBuilder<R, T> in(Function<R, Collection<V>> getValFun, E3Function<T, V> getQueryField) {
        Optional.ofNullable(this.r).map(getValFun).filter(CollectionUtils::isNotEmpty).ifPresent(queryVal -> {
            if (queryVal.size() == 1) {
                queryVal.stream().filter(Objects::nonNull).findFirst().ifPresent(val -> this.conditions.add(new Condition(SqlKeyword.EQ, getQueryField, val)));
            } else {
                this.conditions.add(new Condition(SqlKeyword.IN, getQueryField, queryVal));
            }
        });
        return this;
    }

    public <V> QueryConditionBuilder<R, T> in(SFunction<T, V> getQueryField, Collection<?> rs) {
        this.queryChainWrapper.in(CollectionUtil.isNotEmpty(rs), getQueryField, rs);
        return this;
    }

    /**
     * 日期范围条件设置
     *
     * @param getStartDateFun 开始日期
     * @param getEndDateFun   结束日期
     * @param getQueryField   查询字段
     */
    public QueryConditionBuilder<R, T> dateBetween(Function<R, Date> getStartDateFun, Function<R, Date> getEndDateFun, E3Function<T, Date> getQueryField) {
        return this.dateBetween(getStartDateFun, getEndDateFun, getQueryField, false);
    }

    public QueryConditionBuilder<R, T> dateBetween(Function<R, Date> getStartDateFun, Function<R, Date> getEndDateFun, E3Function<T, Date> getQueryField, boolean isFormat) {
        Optional<R> optional = Optional.ofNullable(this.r);
        optional.map(getStartDateFun).ifPresent(startDate ->
                optional.map(getEndDateFun).ifPresent(endDate -> {
                    if (isFormat) {
                        this.conditions.add(new Condition(getQueryField,
                                DATE_TIME_FORMATTER.format(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().with(LocalTime.MIN)),
                                DATE_TIME_FORMATTER.format(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().with(LocalTime.MAX))
                        ));
                    } else {
                        this.conditions.add(new Condition(getQueryField, startDate, endDate));
                    }
                }));
        return this;
    }

    public QueryConditionBuilder<R, T> between(Function<R, ?> getStartFun, Function<R, ?> getEndFun, E3Function<T, ?> getQueryField) {
        Optional<R> optional = Optional.ofNullable(this.r);
        optional.map(getStartFun).ifPresent(start ->
                optional.map(getEndFun).ifPresent(end ->
                        this.conditions.add(new Condition(getQueryField, start, end))));
        return this;
    }

    public <V> QueryConditionBuilder<R, T> le(Function<R, V> getValFun, E3Function<T, V> getQueryField) {
        this.le(getValFun, getQueryField, false);
        return this;
    }

    public <V> QueryConditionBuilder<R, T> ge(Function<R, V> getValFun, E3Function<T, V> getQueryField) {
        this.ge(getValFun, getQueryField, false);
        return this;
    }

    public <V> QueryConditionBuilder<R, T> le(Function<R, V> getValFun, E3Function<T, V> getQueryField, boolean isFormat) {
        Optional.ofNullable(this.r).map(getValFun).ifPresent(queryVal -> {
            if (isFormat && queryVal instanceof Date) {
                this.conditions.add(new Condition(SqlKeyword.LE, getQueryField, DATE_TIME_FORMATTER.format(((Date) queryVal).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().with(LocalTime.MIN))));
            } else {
                this.conditions.add(new Condition(SqlKeyword.LE, getQueryField, queryVal));
            }
        });
        return this;
    }

    public <V> QueryConditionBuilder<R, T> ge(Function<R, V> getValFun, E3Function<T, V> getQueryField, boolean isFormat) {
        Optional.ofNullable(this.r).map(getValFun).ifPresent(queryVal -> {
            if (isFormat && queryVal instanceof Date) {
                this.conditions.add(new Condition(SqlKeyword.GE, getQueryField, DATE_TIME_FORMATTER.format(((Date) queryVal).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().with(LocalTime.MAX))));
            } else {
                this.conditions.add(new Condition(SqlKeyword.GE, getQueryField, queryVal));
            }
        });
        return this;
    }


    /**
     * 单值模糊搜索,多值精确搜索
     * <p>
     * 多值用空格或逗号分割
     *
     * @param getValFun     查询值
     * @param getQueryField 查询字段
     */
    public QueryConditionBuilder<R, T> singleLikeMultiIn(Function<R, String> getValFun, E3Function<T, String> getQueryField) {
        Optional.ofNullable(this.r).map(getValFun).filter(StringUtils::isNotEmpty).ifPresent(queryVal -> {
            String[] valAry = getSingleLikeMultiIn(queryVal);
            if (valAry.length > 1) {
                this.conditions.add(new Condition(SqlKeyword.IN, getQueryField, Lists.newArrayList(valAry)));
            } else {
                this.conditions.add(new Condition(SqlKeyword.LIKE, getQueryField, queryVal));
            }
        });
        return this;
    }


    public QueryConditionBuilder<R, T> notLike(Function<R, String> getValFun, E3Function<T, String> getQueryField) {
        Optional.ofNullable(this.r).map(getValFun).filter(StringUtils::isNotEmpty).ifPresent(queryVal -> {
            String[] valAry = getSingleLikeMultiIn(queryVal);
            if (valAry.length > 1) {
                this.conditions.add(new Condition(SqlKeyword.NOT_IN, getQueryField, Lists.newArrayList(valAry)));
            } else {
                this.conditions.add(new Condition(SqlKeyword.NOT_LIKE, getQueryField, queryVal));
            }
        });
        return this;
    }

    public <V> QueryConditionBuilder<R, T> notIn(Function<R, Collection<V>> getValFun, E3Function<T, V> getQueryField) {
        Optional.ofNullable(this.r).map(getValFun).filter(CollectionUtils::isNotEmpty).ifPresent(queryVal -> {
            if (queryVal.size() == 1) {
                queryVal.stream().filter(Objects::nonNull).findFirst().ifPresent(val -> this.conditions.add(new Condition(SqlKeyword.NE, getQueryField, val)));
            } else {
                this.conditions.add(new Condition(SqlKeyword.NOT_IN, getQueryField, queryVal));
            }
        });
        return this;
    }



    public <C, V> QueryConditionBuilder<R, T> exists(Class<C> clazz,
                                                     E3Function<C, V> getAssociatedQueryFieldA,
                                                     E3Function<T, V> getAssociatedQueryFieldB,
                                                     Supplier<QueryConditionBuilder<R, C>> supplier) {
        if (Objects.nonNull(supplier)) {
            StringBuilder sqlWhere = new StringBuilder();
            supplier.get().conditions.forEach(condition -> {
                if (SqlKeyword.EQ.equals(condition.getType()) || SqlKeyword.LIKE.equals(condition.getType())) {
                    sqlWhere.append(String.format(" AND %s = '%s'", condition.getColumn().columnToString(), condition.getValue()));
                } else if (SqlKeyword.IN.equals(condition.getType())) {
                    sqlWhere.append(String.format(" AND %s IN %s", condition.getColumn().columnToString(), ((Collection<?>) condition.getValue()).stream().map(i -> SqlScriptUtils.SINGLE_QUOTE + i + SqlScriptUtils.SINGLE_QUOTE).collect(Collectors.joining(",", "(", ")"))));
                } else if (SqlKeyword.BETWEEN.equals(condition.getType())) {
                    sqlWhere.append(String.format(" AND %s between '%s' AND '%s'", condition.getColumn().columnToString(), condition.getStartValue(), condition.getEndValue()));
                }
            });

            if (StringUtils.isNotEmpty(sqlWhere)) {
                String sql = String.format(SQL_FORMAT, TableInfoHelper.getTableInfo(clazz).getTableName(), getAssociatedQueryFieldA.columnToString(), TableInfoHelper.getTableInfo(getAssociatedQueryFieldB.getImplClass()).getTableName() + "." + getAssociatedQueryFieldB.columnToString(), sqlWhere.toString());
                this.queryChainWrapper.exists(sql);
            }
        }
        return this;
    }

    public <C, V> QueryConditionBuilder<R, T> exists(Class<C> clazz,
                                                     E3Function<C, V> getAssociatedQueryFieldA,
                                                     E3Function<T, V> getAssociatedQueryFieldB,
                                                     Function<R, String> getValFun,
                                                     E3Function<C, ?> getQueryField) {
        Optional.ofNullable(this.r).map(getValFun).filter(StringUtils::isNotEmpty).ifPresent(queryVal -> {
            String[] valAry = getSingleLikeMultiIn(queryVal);
            String sqlWhere;
            if (valAry.length > 1) {
                sqlWhere = String.format(" AND %s IN %s", getQueryField.columnToString(), Arrays.stream(valAry).map(i -> SqlScriptUtils.SINGLE_QUOTE + i + SqlScriptUtils.SINGLE_QUOTE).collect(Collectors.joining(",", "(", ")")));
            } else {
                sqlWhere = String.format(" AND %s = '%s'", getQueryField.columnToString(), queryVal);
            }

            String sql = String.format(SQL_FORMAT, TableInfoHelper.getTableInfo(clazz).getTableName(), getAssociatedQueryFieldA.columnToString(), TableInfoHelper.getTableInfo(getAssociatedQueryFieldB.getImplClass()).getTableName() + "." + getAssociatedQueryFieldB.columnToString(), sqlWhere);
            this.queryChainWrapper.exists(sql);
        });
        return this;
    }

    private String[] getSingleLikeMultiIn(String s) {
        return s.split(REGEX);
    }

    private void setCondition() {
        if (CollectionUtils.isNotEmpty(conditions)) {
            conditions.forEach(condition -> {
                if (SqlKeyword.EQ.equals(condition.getType())) {
                    this.queryWrapper.eq(condition.getColumn(), condition.getValue());
                } else if (SqlKeyword.LIKE.equals(condition.getType())) {
                    this.queryWrapper.like(condition.getColumn(), condition.getValue());
                } else if (SqlKeyword.IN.equals(condition.getType())) {
                    this.queryWrapper.in(condition.getColumn(), (Collection<?>) condition.getValue());
                } else if (SqlKeyword.BETWEEN.equals(condition.getType())) {
                    this.queryWrapper.between(condition.getColumn(), condition.getStartValue(), condition.getEndValue());
                } else if (SqlKeyword.LT.equals(condition.getType())) {
                    this.queryWrapper.lt(condition.getColumn(), condition.getValue());
                } else if (SqlKeyword.GT.equals(condition.getType())) {
                    this.queryWrapper.gt(condition.getColumn(), condition.getValue());
                } else if (SqlKeyword.LE.equals(condition.getType())) {
                    this.queryWrapper.le(condition.getColumn(), condition.getValue());
                } else if (SqlKeyword.GE.equals(condition.getType())) {
                    this.queryWrapper.ge(condition.getColumn(), condition.getValue());
                } else if (SqlKeyword.NOT_IN.equals(condition.getType())) {
                    this.queryWrapper.notIn(condition.getColumn(), condition.getValue());
                } else if (SqlKeyword.NOT_LIKE.equals(condition.getType())) {
                    this.queryWrapper.notLike(condition.getColumn(), condition.getValue());
                }
            });
        }
    }

}
