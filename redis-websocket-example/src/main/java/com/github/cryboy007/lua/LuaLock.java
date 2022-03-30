package com.github.cryboy007.lua;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 单机分布式锁，不可重入
 *
 * @author huan.fu 2021/10/26 - 下午3:41
 */
@Component
public class LuaLock {
    
    private static final RedisScript<Boolean> SCRIPT_LOCK = RedisScript.of(new ClassPathResource("script/lock.lua"), Boolean.class);
    private static final RedisScript<Boolean> SCRIPT_UNLOCK = RedisScript.of(new ClassPathResource("script/unlock.lua"), Boolean.class);
    
    @Resource
    private RedisTemplate<String, Object> jsonRedisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    public boolean tryLock(String lockKey, String lockValue, long expire) {
        return lock(lockKey, lockValue, expire);
    }
    
    public Boolean lock(String lockKey, String lockValue, long expire) {
        long now = System.currentTimeMillis();
        long expireTime = System.currentTimeMillis() + expire;
        while (now < expireTime) {
            Boolean lock = jsonRedisTemplate.execute(SCRIPT_LOCK, Collections.singletonList(lockKey), lockValue, expire);
            if (lock != null && lock.equals(true)) {
                return true;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(100);
                now += 100;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }
    
    public Boolean unLock(String lockKey, String lockValue) {
        return jsonRedisTemplate.execute(SCRIPT_UNLOCK, Collections.singletonList(lockKey), lockValue);
    }
}
