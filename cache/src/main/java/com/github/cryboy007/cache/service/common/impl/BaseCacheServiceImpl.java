package com.github.cryboy007.cache.service.common.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cryboy007.cache.model.Person;
import com.github.cryboy007.cache.model.PersonReqQuery;
import com.github.cryboy007.cache.service.CacheService;
import com.github.cryboy007.cache.service.annotation.Cache;
import com.github.cryboy007.cache.service.common.CacheConditionBuilder;
import com.github.cryboy007.cache.service.common.E3Function;
import com.github.cryboy007.cache.service.common.IBaseCacheService;
import com.github.cryboy007.cache.service.common.QueryConditionBuilder;
import com.github.cryboy007.exception.BizCode;
import com.github.cryboy007.exception.BizException;
import com.github.cryboy007.utils.CommonConvertUtil;
import com.google.common.cache.LoadingCache;
import com.sun.istack.internal.NotNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @ClassName BaseCacheServiceImpl
 * @Author tao.he
 * @Since 2022/3/29 18:20
 */
@Slf4j
public abstract class BaseCacheServiceImpl <M extends BaseMapper<T>, T, D, R, Q>
        extends ServiceImpl<M, T>
        implements IBaseCacheService<T, D, R, Q>, CacheService<T> {

    protected final Class<T> entityClass;
    protected final Class<R> rClass;

    protected  Class<? extends BaseCacheServiceImpl> baseMapperClass = null;

    protected boolean isUseCache = false;

    public  LoadingCache<String,List<T>> cache;

    @Autowired
    public void setBean(LoadingCache cache) {
        this.cache = cache;
    }

    public BaseCacheServiceImpl useCache(boolean isUseCache) {
        this.isUseCache = isUseCache;
        return this;
    }

    @Autowired
    protected BaseCacheServiceImpl() {
        Type[] actualTypeArguments = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        this.entityClass = (Class<T>) actualTypeArguments[1];
        this.rClass = (Class<R>) actualTypeArguments[3];
        boolean isCache = this.getClass().isAnnotationPresent(Cache.class);
        // 获取泛型参数T的class
        Type genType = this.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (params.length == 0) {
            log.error("继承BaseDao及BaseDaoImpl时必须添加泛型！！");
        }
        if (isCache) {
            isUseCache = true;
            Cache cache = this.getClass().getAnnotation(Cache.class);
            baseMapperClass = (Class<? extends BaseCacheServiceImpl>) cache.value();
        }
        //存放数据 不是代理对象,getData() 为空
        //cache.put(params[0].getClass(),this.getData());
    }



    @Override
    public void refresh(String key) {
        //todo 如果是分布式项目 则需要通过mq的方式通知刷新消费
        cache.refresh(key);
    }

    public List<T> getData() {
        return list();
    };

    @Override
    public List<T> find(Q req) {
        QueryConditionBuilder<Q, T> builder = getQueryConditionBuilder(req);
        return isUseCache ? req == null ? cacheData() : getCacheConditionBuilder(req).buildList() : builder.buildList() ;
    }

    @Override
    public R get(Q req) {
        QueryConditionBuilder<Q, T> builder = getQueryConditionBuilder(req);
        return isUseCache ? getCacheConditionBuilder(req).buildOne(rClass) : builder.buildOne(rClass);
    }

    @Override
    public R get(Long id) {
        if (Objects.isNull(id)) {
            return null;
        }
        CacheConditionBuilder<Q, T> builder = new CacheConditionBuilder<>(cacheData(),entityClass);
        //builder.getById(id);
        //cacheData().stream().forEach(item ->);
        return isUseCache ? CommonConvertUtil.convertTo(builder.getById(id),rClass) : CommonConvertUtil.convertTo(super.getById(id), rClass);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BizException(BizCode.INVALID_ARGS);
        }
        this.removeByIds(ids);
        if (baseMapperClass != null) {
            this.refresh(this.baseMapperClass.getName());
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByBillIds(Set<Long> billIds) {
        if (CollectionUtils.isEmpty(billIds) || Objects.isNull(this.getBillIdFun())) {
            throw new BizException(BizCode.INVALID_ARGS);
        }
        super.lambdaUpdate().in(this.getBillIdFun(), billIds).remove();
        if (baseMapperClass != null) {
            this.refresh(this.baseMapperClass.getName());
        }
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
        boolean success = super.saveBatch(entityList);
        if (baseMapperClass != null) {
            this.refresh(this.baseMapperClass.getName());
        }
        return success;
    }


    @Override
    public boolean updateBatchById(Collection<T> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new BizException(BizCode.INVALID_ARGS);
        }
        this.processSaveAndUpdateData(entityList);
        boolean success = super.updateBatchById(entityList);
        if (baseMapperClass != null) {
            this.refresh(this.baseMapperClass.getName());
        }
        return success;
    }



    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new BizException(BizCode.INVALID_ARGS);
        }
        this.processSaveAndUpdateData(entityList);
        boolean success = super.saveOrUpdateBatch(entityList);
        if (baseMapperClass != null) {
            this.refresh(this.baseMapperClass.getName());
        }
        return success;
    }

    @NotNull
    protected QueryConditionBuilder<Q, T> getQueryConditionBuilder(Q req) {
        QueryConditionBuilder<Q, T> builder = new QueryConditionBuilder<>(this.lambdaQuery(), req);
        this.setQueryConditions(builder);
        return builder;
    }

    @NotNull
    protected CacheConditionBuilder<Q, T> getCacheConditionBuilder(Q req) {
        CacheConditionBuilder<Q, T> builder = new CacheConditionBuilder<>(cacheData(), req);
        this.setCacheConditions(builder);
        return builder;
    }

    @SneakyThrows
    private List<T> cacheData() {
        return cache.get(this.baseMapperClass.getName());
    }

    protected abstract void setIdCacheCondition(CacheConditionBuilder<Q,T> builder);


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

    protected abstract void setCacheConditions(CacheConditionBuilder<Q,T> builder);
    /**
     * 获取当前代理对象
     */
    protected <P> P getAopContextCurrentProxy(Class<P> clazz) {
        return (P) AopContext.currentProxy();
    }


    @SneakyThrows
    private List<T> getCache() {
        return cache.get(this.baseMapperClass.getName());
        //return JSON.parseArray(s, entityClass);
    }

}
