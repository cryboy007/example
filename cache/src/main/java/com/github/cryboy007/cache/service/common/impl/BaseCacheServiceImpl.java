package com.github.cryboy007.cache.service.common.impl;

import com.alibaba.fastjson.util.TypeUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cryboy007.cache.model.Person;
import com.github.cryboy007.cache.service.CacheService;
import com.github.cryboy007.cache.service.cache.MyCacheLoader;
import com.github.cryboy007.cache.service.common.BaseCacheMapper;
import com.github.cryboy007.cache.service.common.E3Function;
import com.github.cryboy007.cache.service.common.IBaseCacheService;
import com.github.cryboy007.cache.service.common.QueryConditionBuilder;
import com.github.cryboy007.exception.BizCode;
import com.github.cryboy007.exception.BizException;
import com.github.cryboy007.utils.CommonConvertUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;
import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @ClassName BaseCacheServiceImpl
 * @Author tao.he
 * @Since 2022/3/29 18:20
 */
@Slf4j
public abstract class BaseCacheServiceImpl <M extends BaseCacheMapper<T>, T, D, R, Q>
        extends ServiceImpl<M, T>
        implements IBaseCacheService<T, D, R, Q>, CacheService {

    protected final Class<T> entityClass;
    protected final Class<R> rClass;

    public static LoadingCache cache;


    protected BaseCacheServiceImpl() {
        Type[] actualTypeArguments = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        this.entityClass = (Class<T>) actualTypeArguments[1];
        this.rClass = (Class<R>) actualTypeArguments[3];

        // LoadingCache是Cache的缓存实现
        cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .refreshAfterWrite(2, TimeUnit.MINUTES)
                .recordStats()
                .build(new MyCacheLoader());
        // 获取泛型参数T的class
        Type genType = this.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (params.length == 0) {
            log.error("继承BaseDao及BaseDaoImpl时必须添加泛型！！");
        }
        //存放数据 不是代理对象,getData() 为空
        //cache.put(params[0].getClass(),this.getData());
    }



    @Override
    public void refresh(String key) {
        cache.refresh(key);
    }

    public List<T> getData() {
        return list();
    };

    @Override
    public List<T> find(Q req) {
        QueryConditionBuilder<Q, T> builder = getQueryConditionBuilder(req);
        return builder.buildList();
    }

    @Override
    public R get(Q req) {
        QueryConditionBuilder<Q, T> builder = getQueryConditionBuilder(req);
        R r = builder.buildOne(rClass);
        return r;
    }

    @Override
    public R get(Long id) {
        if (Objects.isNull(id)) {
            return null;
        }

        R r = CommonConvertUtil.convertTo(super.getById(id), rClass);
        return r;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BizException(BizCode.INVALID_ARGS);
        }
        this.removeByIds(ids);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByBillIds(Set<Long> billIds) {
        if (CollectionUtils.isEmpty(billIds) || Objects.isNull(this.getBillIdFun())) {
            throw new BizException(BizCode.INVALID_ARGS);
        }
        super.lambdaUpdate().in(this.getBillIdFun(), billIds).remove();
    }

    @Override
    public List<T> findByBillId(Long billId) {
        if (Objects.isNull(billId) || Objects.isNull(this.getBillIdFun())) {
            return Collections.emptyList();
        }
        return super.lambdaQuery().in(this.getBillIdFun(), billId).list();
    }


    @Override
    public boolean saveBatch(Collection<T> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new BizException(BizCode.INVALID_ARGS);
        }
        this.processSaveAndUpdateData(entityList);
        return super.saveBatch(entityList);
    }


    @Override
    public boolean updateBatchById(Collection<T> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new BizException(BizCode.INVALID_ARGS);
        }
        this.processSaveAndUpdateData(entityList);
        return super.updateBatchById(entityList);
    }



    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new BizException(BizCode.INVALID_ARGS);
        }
        this.processSaveAndUpdateData(entityList);
        return super.saveOrUpdateBatch(entityList);
    }

    @NotNull
    protected QueryConditionBuilder<Q, T> getQueryConditionBuilder(Q req) {
        QueryConditionBuilder<Q, T> builder = new QueryConditionBuilder<>(this.lambdaQuery(), req);
        this.setQueryConditions(builder);
        return builder;
    }


    protected abstract E3Function<T, Long> getIdFun();


    protected E3Function<T, Long> getBillIdFun() {
        return null;
    }

    protected void processSaveAndUpdateData(Collection<T> entityList) {
    }


    protected QueryConditionBuilder<Q, T> getQueryConditionBuilder(LambdaQueryWrapper<T> queryWrapper, Q req) {
        QueryConditionBuilder<Q, T> builder = new QueryConditionBuilder<>(this.getBaseMapper(), queryWrapper, req);
        this.setQueryConditions(builder);
        return builder;
    }

    protected abstract void setQueryConditions(QueryConditionBuilder<Q, T> builder);

    /**
     * 获取当前代理对象
     */
    protected <P> P getAopContextCurrentProxy(Class<P> clazz) {
        return (P) AopContext.currentProxy();
    }


}
