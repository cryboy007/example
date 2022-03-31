package com.github.cryboy007.cache.service.common;

import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.baomidou.mybatisplus.core.toolkit.StringUtils.IS;

/**
 * <p>
 * support模块 Function
 * </p>
 *
 * @author lei.mao
 * @since 2021-02-02
 */
public interface E3Function<T, R> extends SFunction<T, R>, Function<T, R> {

    default String getColumn() {
        LambdaMeta lambdaMeta = LambdaUtils.extract(this);
        /**
         * 3.1.0版本
         * SerializedLambda lambda = LambdaUtils.resolve(this);
         *         return StringUtils.resolveFieldName(lambda.getImplMethodName());
         */
        String implMethodName = lambdaMeta.getImplMethodName();
        return resolveFieldName(implMethodName);
    }

    default String columnToString() {
        /**
         *         SerializedLambda lambda = LambdaUtils.resolve(this);
         *         String fieldName = StringUtils.resolveFieldName(lambda.getImplMethodName());
         *         String entityClassName = lambda.getImplClassName();
         *         Map<String, ColumnCache> columnMap = LambdaUtils.getColumnMap(entityClassName);
         */
        LambdaMeta lambdaMeta = LambdaUtils.extract(this);
        Class<?> entityClassName = lambdaMeta.getInstantiatedClass();
        String fieldName = resolveFieldName(lambdaMeta.getImplMethodName());
        Map<String, ColumnCache> columnMap = LambdaUtils.getColumnMap(entityClassName);
        return Optional.ofNullable(columnMap.get(fieldName.toUpperCase(Locale.ENGLISH)))
                .map(ColumnCache::getColumn)
                .orElseThrow(() -> ExceptionUtils.mpe("your property named \"%s\" cannot find the corresponding database column name!", fieldName));
    }

    default Class getImplClass() {
        LambdaMeta lambda = LambdaUtils.extract(this);
        return lambda.getInstantiatedClass();
    }

    /**
     * 解析 getMethodName -> propertyName
     *
     * @param getMethodName 需要解析的
     * @return 返回解析后的字段名称
     */
    public static String resolveFieldName(String getMethodName) {
        if (getMethodName.startsWith("get")) {
            getMethodName = getMethodName.substring(3);
        } else if (getMethodName.startsWith(IS)) {
            getMethodName = getMethodName.substring(2);
        }
        // 小写第一个字母
        return StringUtils.firstToLowerCase(getMethodName);
    }

}
