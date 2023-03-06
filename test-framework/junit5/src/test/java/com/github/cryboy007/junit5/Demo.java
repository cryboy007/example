package com.github.cryboy007.junit5;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * @ClassName Demo
 * @Author tao.he
 * @email study.hetao@foxmail.com
 * @Since 2023/2/23 15:58
 */
@Slf4j
public class Demo {
    /**
     * 表示使用了该注解的方法应该在当前类中所有测试方法之前执行（只执行一次），并且它必须是 static方法（除非@TestInstance指定生命周期为Lifecycle.PER_CLASS）
     */
    @BeforeAll
    public static void beforeAll() {
        log.info("beforeAll");
    }

    @AfterAll
    public static void afterAll() {
        log.info("afterAll");
    }
//
//    @BeforeEach 表示使用了该注解的方法应该在当前类中每一个测试方法之前执行
//    @AfterEach 表示使用了该注解的方法应该在当前类中每一个测试方法之后执行


    @Test
    public void testMethod() {
        log.info("test-method");
    }

    @Test
    @Disabled //用于禁用（或者说忽略）一个测试类或测试方法
    public void ignoreMethod() {
        log.info("ignoreMethod");
    }

    @Test
    @Tag("标签")
    public void tagMethod() {
        log.info("tag-method");
    }



    @BeforeAll
    static void initAll() {
        System.out.println("BeforeAll");
    }

    @BeforeEach
    void init() {
        System.out.println("BeforeEach");
    }

    @Test
    void succeedingTest() {
        System.out.println("succeedingTest");
    }

    @Test
    void failingTest() {
        System.out.println("failingTest");
        fail("a failing test");
    }

    @Test
    @Disabled("for demonstration purposes")
    void skippedTest() {
        // not executed
    }

    @Test
    void abortedTest() {
        System.out.println("abortedTest");
        assumeTrue("abc".contains("Z"));
        fail("test should have been aborted");
    }

    @AfterEach
    void tearDown() {
        System.out.println("AfterEach");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("AfterEach");
    }

}
