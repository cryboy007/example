package com.github.cryboy007.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @ Author     ：pengfei.zhu
 * @ Date       ：Created in 19:52 2019/11/5
 * @ Description：获取spring上下文
 * @ Modified By：
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {


    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
       applicationContext = context;
    }

    /**
   * 获取applicationContext对象
   * @return
   */
    public static ApplicationContext getApplicationContext() {
      return applicationContext;
    }


    /**
     *  根据bean的id来查找对象
     *  @param id
     *  @return
     *
     */
    public static <T> T getBeanById(String id) {
        return (T) applicationContext.getBean(id);
    }

    /**
     * 根据bean的class来查找对象
     * @param c
     * @return
     *
     */
    public static <T> T getBeanByClass(Class c) {
        return (T) applicationContext.getBean(c);
    }

}
