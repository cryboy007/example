package com.github.cryboy007.service;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @ClassName MockCalssTest
 * @Author tao.he
 * @email study.hetao@foxmail.com
 * @Since 2023/2/24 10:26
 */
public class MockCalssTest {

    @Test
    public void mockClassTest() {
        Random mockRandom = mock(Random.class);
        // 默认值: mock 对象的方法的返回值默认都是返回类型的默认值
        System.out.println(mockRandom.nextInt());
        System.out.println(mockRandom.nextBoolean());
        System.out.println(mockRandom.nextDouble());
        // mock: 指定调用 nextInt 方法时，永远返回 100
        when(mockRandom.nextInt()).thenReturn(100);
        Assert.assertEquals(100, mockRandom.nextInt());
        Assert.assertEquals(100, mockRandom.nextInt());
    }

    @Test
    public void mockInterfaceTest() {
        List mockList = mock(List.class);
        // 接口的默认值：和类方法一致，都是默认返回值
        Assert.assertEquals(0, mockList.size());
        Assert.assertNull(mockList.get(0));
        // 注意：调用 mock 对象的写方法，是没有效果的
        mockList.add("a");
        Assert.assertEquals(0, mockList.size());      // 没有指定 size() 方法返回值，这里结果是默认值
        Assert.assertNull(mockList.get(0));   // 没有指定 get(0) 返回值，这里结果是默认值

        // mock值测试
        when(mockList.get(0)).thenReturn("a");          // 指定 get(0)时返回 a
        Assert.assertEquals(0, mockList.size());        // 没有指定 size() 方法返回值，这里结果是默认值
        Assert.assertEquals("a", mockList.get(0));      // 因为上面指定了 get(0) 返回 a，所以这里会返回 a
        Assert.assertNull(mockList.get(1));     // 没有指定 get(1) 返回值，这里结果是默认值

    }
}
