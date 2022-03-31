package com.github.cryboy007.cache.service.cache;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.cryboy007.cache.service.common.impl.BaseCacheServiceImpl;
import com.github.cryboy007.utils.SpringContextUtil;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * @ClassName MyCacheLoader
 * @Author tao.he
 * @Since 2022/3/29 17:26
 */
public class MyCacheLoader extends CacheLoader<String,String> {
    @Override
    public String load(String key) throws Exception {
        BaseCacheServiceImpl baseMapper = (BaseCacheServiceImpl) SpringContextUtil.getBeanByClass(Class.forName(key));
        return baseMapper.getData();
    }

    @Override
    public ListenableFuture<String> reload(String key, String oldValue) throws Exception {
        return super.reload(key, oldValue);
    }
}
