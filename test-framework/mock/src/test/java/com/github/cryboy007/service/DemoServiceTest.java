package com.github.cryboy007.service;

import com.github.cryboy007.dao.DemoDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DemoServiceTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getDemoStatus() {
        // mock DemoDao instance
        DemoDao mockDemoDao = Mockito.mock(DemoDao.class);
        // 使用 mockito 对 getDemoStatus 方法打桩
        Mockito.when(mockDemoDao.getDemoStatus()).thenReturn(1);
        // 调用 mock 对象的 getDemoStatus 方法，结果永远是 1
        Assert.assertEquals(1, mockDemoDao.getDemoStatus());
        DemoService demoService = new DemoService(mockDemoDao);
        Assert.assertEquals(1,demoService.getDemoStatus());
    }
}