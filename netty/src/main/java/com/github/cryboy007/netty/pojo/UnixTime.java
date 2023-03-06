package com.github.cryboy007.netty.pojo;

import java.util.Date;

/**
 * @ClassName UnixTime
 * @Author tao.he
 * @email study.hetao@foxmail.com
 * @Since 2023/3/6 9:28
 */
public class UnixTime {
    private final long value;

    public UnixTime() {
        this(System.currentTimeMillis() / 1000L + 2208988800L);
    }

    public UnixTime(long value) {
        this.value = value;
    }

    public long value() {
        return value;
    }

    @Override
    public String toString() {
        return new Date((value() - 2208988800L) * 1000L).toString();
    }
}
