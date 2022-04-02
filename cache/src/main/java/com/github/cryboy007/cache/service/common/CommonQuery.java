package com.github.cryboy007.cache.service.common;

import org.joor.Reflect;

import java.util.Optional;

/**
 * @InterfaceName CommonQuery
 * @Author HETAO
 * @Date 2022/4/1 18:53
 */
public interface CommonQuery<T extends CommonQuery> {

    default String getValue(T t,String field) {
        return Optional.ofNullable(Reflect.on(t).field(field).get()).orElse("").toString();
    };
}
