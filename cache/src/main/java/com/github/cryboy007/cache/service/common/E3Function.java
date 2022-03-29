package com.github.cryboy007.cache.service.common;

import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

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
        SerializedLambda lambda = LambdaUtils.resolve(this);
        return StringUtils.resolveFieldName(lambda.getImplMethodName());
    }

    default String columnToString() {
        SerializedLambda lambda = LambdaUtils.resolve(this);
        String fieldName = StringUtils.resolveFieldName(lambda.getImplMethodName());
        String entityClassName = lambda.getImplClassName();
        Map<String, ColumnCache> columnMap = LambdaUtils.getColumnMap(entityClassName);

        return Optional.ofNullable(columnMap.get(fieldName.toUpperCase(Locale.ENGLISH)))
                .map(ColumnCache::getColumn)
                .orElseThrow(() -> ExceptionUtils.mpe("your property named \"%s\" cannot find the corresponding database column name!", fieldName));
    }

    default Class getImplClass() {
        SerializedLambda lambda = LambdaUtils.resolve(this);
        return lambda.getImplClass();
    }

}
