package com.github.cryboy007.service;

import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * @ClassName MockitoDemo
 * @Author tao.he
 * @email study.hetao@foxmail.com
 * @Since 2023/2/24 10:45
 */
public class MockitoDemo {
    // 测试 spy  对于@Spy，如果发现修饰的变量是 null，会自动调用类的无参构造函数来初始化。
    @Test
    public void test_spy() {

        ExampleService spyExampleService = spy(new ExampleService());

        // 默认会走真实方法
        Assert.assertEquals(3, spyExampleService.add(1, 2));

        // 打桩后，不会走了
        when(spyExampleService.add(1, 2)).thenReturn(10);
        Assert.assertEquals(10, spyExampleService.add(1, 2));

        // 但是参数比匹配的调用，依然走真实方法
        Assert.assertEquals(3, spyExampleService.add(2, 1));
    }

    // 测试 mock
    @Test
    public void test_mock() {

        ExampleService mockExampleService = mock(ExampleService.class);

        // 默认返回结果是返回类型int的默认值
        Assert.assertEquals(0, mockExampleService.add(1, 2));

    }
}

class ExampleService {

    int add(int a, int b) {
        return a+b;
    }

}
