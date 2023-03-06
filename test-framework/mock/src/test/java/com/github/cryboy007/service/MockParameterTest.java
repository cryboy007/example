package com.github.cryboy007.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Random;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @ClassName MockParameterTest
 * @Author tao.he
 * @email study.hetao@foxmail.com
 * @Since 2023/2/24 10:36
 */
@RunWith(MockitoJUnitRunner.class)
public class MockParameterTest {
    @Mock
    private List<String> testList;

    @Test
    public void test01() {

        // 精确匹配 0
        when(testList.get(0)).thenReturn("a");
        Assert.assertEquals("a", testList.get(0));

        // 精确匹配 0
        when(testList.get(0)).thenReturn("b");
        Assert.assertEquals("b", testList.get(0));

        // 模糊匹配 anyInt 只是用来匹配参数的工具之一，目前 mockito 有多种匹配函数，any
        when(testList.get(anyInt())).thenReturn("c");
        Assert.assertEquals("c", testList.get(0));
        Assert.assertEquals("c", testList.get(4));

    }

    @Mock
    private Random mockRandom;

    @Test
    public void test02() {
        when(mockRandom.nextInt()).thenThrow(new RuntimeException("异常"));
        try {
            mockRandom.nextInt();
            Assert.fail();  // 上面会抛出异常，所以不会走到这里
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof RuntimeException);
            Assert.assertEquals("异常", ex.getMessage());
        }
    }

    /**
     * thenThrow 中可以指定多个异常。在调用时异常依次出现。若调用次数超过异常的数量，再次调用时抛出最后一个异常。
     * 对应返回类型是 void 的函数，thenThrow 是无效的，要使用 doThrow。
     */
    @Test
    public void throwTest2() {

        Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt()).thenThrow(new RuntimeException("异常1"), new RuntimeException("异常2"));

        try {
            mockRandom.nextInt();
            Assert.fail();
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof RuntimeException);
            Assert.assertEquals("异常1", ex.getMessage());
        }

        try {
            mockRandom.nextInt();
            Assert.fail();
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof RuntimeException);
            Assert.assertEquals("异常2", ex.getMessage());
        }
    }
}
