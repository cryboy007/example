package com.github.cryboy007.config;

/**
 * @ClassName CycleGeneratorStreamMessageRunner
 * @Author tao.he
 * @Since 2022/3/30 10:31
 */

import com.github.cryboy007.stream.StreamProducer;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.github.cryboy007.constant.RedisConstant.STREAM_KEY_001;

/**
 * 周期性的向流中产生消息
 */
@Component
@AllArgsConstructor
public class CycleGeneratorStreamMessageRunner implements ApplicationRunner {

    private final StreamProducer streamProducer;

    @Override
    public void run(ApplicationArguments args) {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> streamProducer.sendRecord(STREAM_KEY_001),
                        0, 5, TimeUnit.SECONDS);
    }
}
