package com.github.cryboy007.lua;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * lua 限流操作
 *
 * @author huan.fu 2021/10/27 - 下午2:03
 */
@Component
public class Limit {
    private static final RedisScript<Boolean> SCRIPT_LIMIT = RedisScript.of(new ClassPathResource("script/limit.lua"), Boolean.class);
    @Autowired
    private RedisTemplate<String, Object> jsonRedisTemplate;
    public Boolean limit(String limitKey, long limitCount, long expireMs) {
        return jsonRedisTemplate.execute(SCRIPT_LIMIT, Collections.singletonList(limitKey), limitCount, expireMs);
    }
}
