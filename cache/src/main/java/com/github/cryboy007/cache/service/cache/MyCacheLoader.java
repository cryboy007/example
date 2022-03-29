package com.github.cryboy007.cache.service.cache;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.cryboy007.utils.SpringContextUtil;
import com.google.common.cache.CacheLoader;

/**
 * @ClassName MyCacheLoader
 * @Author tao.he
 * @Since 2022/3/29 17:26
 */
public class MyCacheLoader extends CacheLoader<String,Object> {
    @Override
    public Object load(String key) throws Exception {
        BaseMapper baseMapper = (BaseMapper) SpringContextUtil.getBeanById(key);
        return baseMapper.selectList(null);
    }
}
