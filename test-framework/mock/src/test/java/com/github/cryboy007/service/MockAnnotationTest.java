package com.github.cryboy007.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Random;

import static org.mockito.Mockito.when;

/**
 * @ClassName MockAnnotationTest
 * @Author tao.he
 * @email study.hetao@foxmail.com
 * @Since 2023/2/24 10:34
 */

/**
 * 使用该注解时，要使用MockitoAnnotations.initMocks 方法，让注解生效,
 * 比如放在@Before方法中初始化。比较优雅优雅的写法是用MockitoJUnitRunner，
 * 它可以自动执行MockitoAnnotations.initMocks 方法。
 */
@RunWith(MockitoJUnitRunner.class)
public class MockAnnotationTest {
    @Mock
    private Random random;

    @Test
    public void test() {
        when(random.nextInt()).thenReturn(100);
        Assert.assertEquals(100, random.nextInt());
    }

}
