package com.github.cryboy007.test;

import com.github.cryboy007.lua.LuaLock;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 */
//@Component
public class TestLuaLock implements ApplicationRunner {
    
    @Resource
    private LuaLock luaLock;
    
    private volatile int i;
    
    public void add() {
        i++;
    }
    
    public void print() {
        System.err.println("i的值是: " + i);
    }
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(50);
        String lockKey = "lock:add";
        for (int j = 0; j < 50; j++) {
            executor.submit(() -> {
                try {
                    String lockValue = UUID.randomUUID().toString();
                    Boolean lock = luaLock.lock(lockKey, lockValue, 60000);
                    if (lock) {
                        for (int k = 0; k < 50; k++) {
                            add();
                        }
                        luaLock.unLock(lockKey, lockValue);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        }
        TimeUnit.SECONDS.sleep(5);
        print();
    }
}
