package com.github.cryboy007.service;

import com.github.cryboy007.dao.DemoDao;

/**
 * @ClassName HelloService
 * @Author tao.he
 * @email study.hetao@foxmail.com
 * @Since 2023/2/24 10:18
 */
public class DemoService {
    private DemoDao demoDao;

    public DemoService(DemoDao demoDao) {
        this.demoDao = demoDao;
    }

    public int getDemoStatus(){
        return demoDao.getDemoStatus();
    }
}
