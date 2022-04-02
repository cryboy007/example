package com.github.cryboy007.cache.service.common;

import lombok.SneakyThrows;
import org.joor.Reflect;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @InterfaceName CommonQuery
 * @Author HETAO
 * @Date 2022/4/1 18:53
 */
public interface CommonQuery<T extends CommonQuery> {
    String GET = "get";

    @SneakyThrows
    default String getValue(T t, String field){
        //return Optional.ofNullable(Reflect.on(t).field(field).get()).orElse("").toString();
        //return Optional.ofNullable(Reflect.on(t).call("getName").get()).orElse("").toString();
        //反射方法 比反射field快非常多
        field = GET + field.substring(0,1).toUpperCase()+field.substring(1);
        Method method = ReflectionUtils.findMethod(t.getClass(), field);
        return Optional.ofNullable(method.invoke(t)).orElse("").toString();
    };
}
