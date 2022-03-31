package com.github.cryboy007.cache.service;

import java.util.List;

/**
 * @ClassName CacheService
 * @Author tao.he
 * @Since 2022/3/29 10:04
 */
public interface CacheService<T> {
    void refresh (String key);

    List<T> getData ();
}
