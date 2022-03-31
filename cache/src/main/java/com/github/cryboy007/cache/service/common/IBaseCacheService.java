package com.github.cryboy007.cache.service.common;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.cryboy007.cache.service.PersonService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 公共服务类
 * </p>
 *
 */
public interface IBaseCacheService<T, D, R, Q> extends IService<T> {
    String ASSERTION_ERROR = "不支持方法!";

    default IBaseCacheService useCache(boolean useCache) {
        return this;
    }

    default List<R> query(Q req) {
        throw new AssertionError(ASSERTION_ERROR);
    }

    default List<T> find(Q req) {
        throw new AssertionError(ASSERTION_ERROR);
    }

    default R get(Q req) {
        throw new AssertionError(ASSERTION_ERROR);
    }

    default R get(Long id) {
        throw new AssertionError(ASSERTION_ERROR);
    }

    default Long create(D dto) {
        throw new AssertionError(ASSERTION_ERROR);
    }

    default List<Long> createBatch(List<D> dtoList) {
        throw new AssertionError(ASSERTION_ERROR);
    }

    default void update(D dto) {
        throw new AssertionError(ASSERTION_ERROR);
    }

    default void updateBatch(List<D> dtoList) {
        throw new AssertionError(ASSERTION_ERROR);
    }

    default void delete(Set<Long> ids) {
        throw new AssertionError(ASSERTION_ERROR);
    }

    /**
     * 根据主表Id删除
     *
     * @param billIds 主表Id
     */
    default void deleteByBillIds(Set<Long> billIds) {
        throw new AssertionError(ASSERTION_ERROR);
    }

    default List<T> findByBillId(Long billId) {
        throw new AssertionError(ASSERTION_ERROR);
    }

}
