package com.github.cryboy007.utils.auto_wrapper;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.invoke.*;
import java.lang.reflect.Method;

@Slf4j
public class CreateSFunctionUtil {

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



}
