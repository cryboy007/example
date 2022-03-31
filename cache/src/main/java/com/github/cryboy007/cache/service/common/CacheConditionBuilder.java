package com.github.cryboy007.cache.service.common;

import java.io.Serializable;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.github.cryboy007.cache.model.Person;
import com.github.cryboy007.cache.model.PersonReqQuery;
import com.github.cryboy007.exception.BizCode;
import com.github.cryboy007.exception.BizException;
import com.github.cryboy007.utils.CollectionUtil;
import com.github.cryboy007.utils.CommonConvertUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 查询条件构造器
 * </p>
 *
 * @author lei.mao
 * @since 2021/5/21
 */
@Slf4j
public class CacheConditionBuilder<R, T> {
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

    public CacheConditionBuilder(List<T> cacheData,R r) {
        this.r = r;
        this.cacheData = cacheData;
    }

    public CacheConditionBuilder(List<T> cacheData,Class<T> entityClass) {
        this.cacheData = cacheData;
        this.entityClass = entityClass;
    }

    public <V> CacheConditionBuilder<R,T> eq(Function<R, V> getValFun, Predicate<T> condition) {
        Optional.ofNullable(this.r).map(getValFun).ifPresent(queryVal -> {
            log.info("比较的值:{}",queryVal);
        });
        this.cacheData = this.cacheData.stream().filter(condition).collect(Collectors.toList());
        return this;
    }

    public List<T> buildList() {
        if (Objects.isNull(this.r)) {
            throw new BizException(BizCode.INVALID_ARGS);
        }
        return this.cacheData;
    }


    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();


    @SuppressWarnings("unchecked")
    public static <T> SFunction<T, ?> createSFunction(Class<T> clazz, Method method){
        try {
            final MethodHandle getMethodHandle = lookup.unreflect(method);
            //动态调用点
            final CallSite getCallSite = LambdaMetafactory.altMetafactory(
                    lookup
                    , "apply"
                    , MethodType.methodType(SFunction.class)
                    , MethodType.methodType(Object.class, Object.class)
                    , getMethodHandle
                    , MethodType.methodType(Object.class, clazz)
                    , LambdaMetafactory.FLAG_SERIALIZABLE
                    , Serializable.class
            );
            return  (SFunction<T, ?>)getCallSite.getTarget().invokeExact();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        log.error("SFunction 创建失败! {},{}",clazz,method);
        return null;
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
        Field field = null;
        try {
            field = entityClass.getDeclaredField("id");
            Field finalField = field;
            this.cacheData = this.cacheData.stream().filter(item -> {
                Long value = null;
                try {
                    finalField.setAccessible(true);
                    value = (Long) finalField.get(item);
                    //log.info("获取的主键:{}",value);
                   /* Method method = entityClass.getMethod("getId");
                    Long objValue = (Long)method.invoke(item);*/
                    if (id.equals(value)) {
                        return true;
                    }
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return false;
            }).collect(Collectors.toList());
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(this.cacheData.get(0)).orElse(null);
    }

    @Data
    class Condition {
        private SqlKeyword type;
        private E3Function<T, ?> column;
        private Object value;

        private Object startValue;
        private Object endValue;

        public Condition(SqlKeyword type, E3Function<T, ?> column, Object value) {
            this.type = type;
            this.column = column;
            this.value = value;
        }

        public Condition(E3Function<T, ?> column, Object startValue, Object endValue) {
            this.type = SqlKeyword.BETWEEN;
            this.column = column;
            this.startValue = startValue;
            this.endValue = endValue;
        }
    }

}
