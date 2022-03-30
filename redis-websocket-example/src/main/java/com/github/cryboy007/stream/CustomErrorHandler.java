package com.github.cryboy007.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ErrorHandler;

/**
 * @ClassName CustomErrorHandler
 * @Author tao.he
 * @Since 2022/3/30 10:41
 */
@Slf4j
public class CustomErrorHandler implements ErrorHandler {
    @Override
    public void handleError(Throwable t) {
        log.error("发生了异常", t);
    }
}
