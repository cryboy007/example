package com.github.cryboy007.cache.inteceptor;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @ClassName NoArgsWhereInterceptor
 * @Author tao.he
 * @Since 2022/4/6 11:55
 */
@Component
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
@Slf4j
public class NoArgsWhereInterceptor implements Interceptor {

    public static final String PARAMETER_OBJECT_KEY = "_parameter";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        /*
         * 由于一些分页查询方法在标识分页之前会执行一些其他的查询操作,这里加上一层分页标识判断
         */
        if (Objects.isNull(PageHelper.getLocalPage())) {
            return invocation.proceed();
        }

        if (NoArgsWhereHelper.getLocal()) {
            return invocation.proceed();
        }

        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];

        if (!SqlCommandType.SELECT.equals(ms.getSqlCommandType())) {
            return invocation.proceed();
        }
        try {
            Object parameter = args[1];
            RowBounds rowBounds = (RowBounds) args[2];
            ResultHandler resultHandler = (ResultHandler) args[3];
            Executor executor = (Executor) invocation.getTarget();
            CacheKey cacheKey;
            BoundSql boundSql;
            //由于逻辑关系，只会进入一次
            if (args.length == 4) {
                //4 个参数时
                boundSql = ms.getBoundSql(parameter);
                cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
            } else {
                //6 个参数时
                cacheKey = (CacheKey) args[4];
                boundSql = (BoundSql) args[5];
            }
            String sql = boundSql.getSql();
            log.info("解析sql:"+sql);
            BoundSql dataScopeBoundSql;
            if (!sql.toUpperCase().contains("WHERE") && !sql.toUpperCase().contains("LIMIT")) {
                String replaceSql = sql + " limit 0,1000";
                log.error("查询未使用查询条件和分页.使用默认规则");
                dataScopeBoundSql = new BoundSql(ms.getConfiguration(), replaceSql, boundSql.getParameterMappings(), parameter);
                return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, dataScopeBoundSql);
              /*  //动态参数
                MetaObject metaObject = ms.getConfiguration().newMetaObject(parameter);
                List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
                log.error("查询未使用查询条件和分页.请添加");
                //return Lists.newArrayList();
                return invocation.proceed();*/
            }
            return invocation.proceed();
        }finally {
            NoArgsWhereHelper.clear();
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
