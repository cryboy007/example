package com.github.cryboy007.cache.service.common;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import lombok.Data;

/**
 * @ClassName Condition
 * @Author tao.he
 * @Since 2022/4/1 17:58
 */
@Data
public class Condition<T,R> {
    private SqlKeyword type;
    private E3Function<T, ?> column;
    private Object value;

    private Object startValue;
    private Object endValue;

    public Condition(SqlKeyword type, E3Function<T, ?> column, Object value) {
        this.type = type;
        this.column = column;
        this.value = value;
    }

    public Condition(E3Function<T, ?> column, Object startValue, Object endValue) {
        this.type = SqlKeyword.BETWEEN;
        this.column = column;
        this.startValue = startValue;
        this.endValue = endValue;
    }
}