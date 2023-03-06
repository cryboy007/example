package com.github.cryboy007.junit5;

/**
 * @ClassName AssertDemo
 * @Author tao.he
 * @email study.hetao@foxmail.com
 * @Since 2023/2/23 16:28
 */

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * assertEquals 断言预期值和实际值相等assertAll 分组断言,
 * 执行其中包含的所有断言assertArrayEquals 断言预期数组和实际数组相等assertFalse
 * 断言条件为假assertNotNull 断言不为空assertSame 断言两个对象相等assertTimeout 断言超时fail 使单元测试失败
 * assertTimeoutPreemptively() 和 assertTimeout() 的区别为: 两者都是断言超时，前者在指定时间没有完成任务就会立即返回断言失败；后者会在任务执行完毕之后才返回
 * ------
 * ------
 */
public class AssertDemo {
    // 标准的测试例子
    @Test
    @DisplayName("Exception Test Demo")
    void assertThrowsException() {
        String str = null;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Integer.valueOf(str);
        });
    }

    // 注:异常失败例子，当Lambda表达式中代码出现的异常会跟首个参数的异常类型进行比较，如果不属于同一类异常，则失败
    @Test
    @DisplayName("Exception Test Demo2")
    void assertThrowsException2() {
        String str = null;
        Assertions.assertThrows(NullPointerException.class, () -> {
            Integer.valueOf(str);
        });
    }
    //嵌套测试
    @Nested
    @DisplayName("when new")
    class WhenNew {

        @Test
        @DisplayName("is empty")
        void isEmpty() {
            assertTrue(true);
        }
    }
}
