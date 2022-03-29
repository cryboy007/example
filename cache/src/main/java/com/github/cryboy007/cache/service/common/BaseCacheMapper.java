package com.github.cryboy007.cache.service.common;

import org.apache.ibatis.annotations.Mapper;

/**
 * @InterfaceName BaseMapper
 * @Author HETAO
 * @Date 2022/3/29 18:19
 */
@Mapper
public interface BaseCacheMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
}
