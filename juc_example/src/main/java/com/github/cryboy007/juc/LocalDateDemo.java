package com.github.cryboy007.juc;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * @ClassName LocalDateDemo
 * @Author tao.he
 * @email study.hetao@foxmail.com
 * @Since 2022/11/18 10:44
 */
public class LocalDateDemo {
    public static void main(String[] args) {
        final LocalDate date = LocalDate.now().plusDays(10);
//        ZoneId zoneId = ZoneId.systemDefault();
//        LocalDate localDate = date.toInstant().atZone(zoneId).toLocalDate();
        final LocalDate now = LocalDate.now();
        final long between = ChronoUnit.DAYS.between(date,now);
        System.out.println(between);
    }
}
