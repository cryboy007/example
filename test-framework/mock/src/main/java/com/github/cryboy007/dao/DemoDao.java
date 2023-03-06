package com.github.cryboy007.dao;

import java.util.Random;

/**
 * @ClassName DemoDao
 * @Author tao.he
 * @email study.hetao@foxmail.com
 * @Since 2023/2/24 10:20
 */
public class DemoDao {
    public int getDemoStatus(){
        return new Random().nextInt();
    }
}
