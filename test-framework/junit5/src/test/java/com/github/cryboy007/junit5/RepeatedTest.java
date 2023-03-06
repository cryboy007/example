package com.github.cryboy007.junit5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.TestInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @ClassName RepeatedTest
 * @Author tao.he
 * @email study.hetao@foxmail.com
 * @Since 2023/2/23 16:33
 */

/**
 * 除了指定重复次数外，还可以通过@RepeatedTest注解的name属性为每次重复配置自定义显示名称。此外，显示名称可以是模式，由静态文本和动态占位符的组合而成。目前支持以下占位符:{displayName}: @RepeatedTest方法的显示名称{currentRepetition}: 当前重复次数{totalRepetitions}: 重复的总次数
 * ------
 */
public class RepeatedTest {

    @BeforeEach
    void beforeEach(TestInfo testInfo, RepetitionInfo repetitionInfo) {
        int currentRepetition = repetitionInfo.getCurrentRepetition();
        int totalRepetitions = repetitionInfo.getTotalRepetitions();
        String methodName = testInfo.getTestMethod().get().getName();
        System.out.println(String.format("About to execute repetition %d of %d for %s", //
                currentRepetition, totalRepetitions, methodName));
    }

    @org.junit.jupiter.api.RepeatedTest(3)
    void repeatedTest() {
        // ...
    }

    @org.junit.jupiter.api.RepeatedTest(2)
    void repeatedTestWithRepetitionInfo(RepetitionInfo repetitionInfo) {
        assertEquals(2, repetitionInfo.getTotalRepetitions());
    }

    @org.junit.jupiter.api.RepeatedTest(value = 1, name = "{displayName} {currentRepetition}/{totalRepetitions}")
    @DisplayName("Repeat!")
    void customDisplayName(TestInfo testInfo) {
        assertEquals(testInfo.getDisplayName(), "Repeat! 1/1");
    }

    @org.junit.jupiter.api.RepeatedTest(value = 1, name = org.junit.jupiter.api.RepeatedTest.LONG_DISPLAY_NAME)
    @DisplayName("Details...")
    void customDisplayNameWithLongPattern(TestInfo testInfo) {
        assertEquals(testInfo.getDisplayName(), "Details... :: repetition 1 of 1");
    }

    @org.junit.jupiter.api.RepeatedTest(value = 2, name = "Wiederholung {currentRepetition} von {totalRepetitions}")
    void repeatedTestInGerman() {
        // ...
    }
}
