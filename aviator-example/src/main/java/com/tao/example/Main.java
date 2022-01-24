package com.tao.example;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.Options;
import lombok.SneakyThrows;

import java.io.IOException;

/**
 * @ClassName Main
 * @Author tao.he
 * @Since 2022/1/22 10:31
 */
public class Main {
    static String dir = "aviator-example/src/main/java/com/tao/example/script/";
    static AviatorEvaluatorInstance engine ;
    static {
        //打开跟踪执行
        engine = AviatorEvaluator.getInstance();
        engine.setOption(Options.TRACE_EVAL,true);
    }

    @SneakyThrows
    public static void main(String[] args) {
        /**
         * 文件路径查找按照下列顺序：
         * ● path 指定的文件系统绝对或者相对路径
         * ● user.dir 项目的根目录开始的相对路径
         * ● classpath 下的绝对和相对路径
         */
        //helloWord();
        //mapParams();
    }

    private static void mapParams() {
        String expression = "a - (b + c) > 100";
        Expression compiledExp = AviatorEvaluator.getInstance().compile(expression);
        boolean result = (boolean)compiledExp.execute(compiledExp.newEnv("a", 200, "b", 50, "c", 40));
        System.out.println(result);
    }

    private static void helloWord() throws IOException {
        System.out.println(System.getProperty("user.dir"));
        Expression expression = AviatorEvaluator.getInstance()
                .compileScript(dir + "hello.av",true);
        expression.execute();
    }

    public static void validate(String script) {
        AviatorEvaluator.validate(script);
    }

}
