package com.github.cryboy007.cache.inteceptor;

import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.github.cryboy007.cache.annotation.EnableTableScan;
import com.github.cryboy007.exception.BizException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
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
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
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
public class NoArgsWhereInterceptor extends JsqlParserSupport implements Interceptor {

    public static final String PARAMETER_OBJECT_KEY = "_parameter";

    private static final Long MAX_PAGE_SIZE = 10000L;


    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        Expression where = plainSelect.getWhere();
       /* if (where == null) {
            throw new BizException("40001","Full table scan! Sql has no where clause .");
        }*/
        Assert.notNull(where,"非法SQL,没有关键字");
        if (where instanceof EqualsTo) {
            Expression leftExpression = ((EqualsTo) where).getLeftExpression();
            Expression rightExpression = ((EqualsTo) where).getRightExpression();
            if (leftExpression.getClass().isAssignableFrom(rightExpression.getClass())) {
                if (leftExpression instanceof StringValue
                        && ((StringValue)leftExpression).getValue().equals(((StringValue)rightExpression).getValue())) {
                    throw new BizException("40002","非法SQL,where 跟随恒等式");
                }
                if (leftExpression instanceof LongValue
                        && ((LongValue)leftExpression).getValue() == (((LongValue)rightExpression).getValue())) {
                    throw new BizException("40002","非法SQL,where 跟随恒等式");
                }
            }
        }
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
       /* *//*
         * 由于一些分页查询方法在标识分页之前会执行一些其他的查询操作,这里加上一层分页标识判断
         *//*
        if (Objects.isNull(PageHelper.getLocalPage())) {
            return invocation.proceed();
        }*/

        if (NoArgsWhereHelper.getLocal()) {
            return invocation.proceed();
        }

        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        if (!SqlCommandType.SELECT.equals(ms.getSqlCommandType())) {
            return invocation.proceed();
        }
        String className = StringUtils.substringBeforeLast(ms.getId(), ".");
        String methodName = StringUtils.substringAfterLast(ms.getId(),".");
        log.debug("类:{},方法:{}",className,methodName);
        Class<?> clazz = ClassUtils.getClass(className);
        EnableTableScan enableTableScan = clazz.getAnnotation(EnableTableScan.class);
        if (enableTableScan != null && enableTableScan.value()) {
            return invocation.proceed();
        }
        if (!methodName.contains("_COUNT")) {
            Method method = ReflectionUtils.findMethod(clazz, methodName);
            enableTableScan = AnnotationUtils.findAnnotation(method, EnableTableScan.class);
            if (enableTableScan != null && enableTableScan.value()) {
                return invocation.proceed();
            }
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
            Page<Object> localPage = PageHelper.getLocalPage();
            if (localPage == null) {
                //校验 where条件
                parserSingle(boundSql.getSql(),parameter);
            }else {
                int pageSize = localPage.getPageSize();
                boolean count = localPage.isCount();
                //是否开启全表count
                if (count) {
                    this.parserSingle(boundSql.getSql(),parameter);
                }
                if (pageSize > MAX_PAGE_SIZE) {
                    throw new BizException("40004","Page Size over " + MAX_PAGE_SIZE);
                }
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
