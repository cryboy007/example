package com.github.cryboy007.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring bean获取工具类
 * @author xiaohui.su
 * @since 2017-7-1
 */
public final class ServiceUtils implements BeanFactoryPostProcessor {

	private static ConfigurableListableBeanFactory beanFactory; // Spring应用上下文环境

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		ServiceUtils.beanFactory = beanFactory;
	}

	public static <T> T getService(String name, Class<T> clazz){
		return beanFactory.getBean(name, clazz);
	}

	public static <T> T getService(Class<T> clazz){
		return beanFactory.getBean(clazz);
	}

	public static Object getService(String name) {

		if (containsBean(name)) {// 避免找不到抛异常
			return beanFactory.getBean(name);
		}

		return null;
	}

	public ServiceUtils(){
	}

	private <T> T getBean(String name, Class<T> clazz){
		return beanFactory.getBean(name, clazz);
	}

	private <T> T getBean(Class<T> clazz) {
		return beanFactory.getBean(clazz);
	}

	private Object getBean(String name){
		return beanFactory.getBean(name);
	}

	public static boolean containsBean(String name) {
		return beanFactory.containsBean(name);
	}
	
	public static <T> List<T> getBeans(Class<T> clazz) {
		Map<String, T> map = new HashMap<String, T>();
		String[] beanNames = beanFactory.getBeanNamesForType(clazz);
		if (beanNames != null) {
			for (String name : beanNames) {
				T bean = getService(name, clazz);
				if (bean != null) {
					map.put(name, bean);
				}
			}
		}
		return new ArrayList<T>(map.values());
	}
	
	/// 获取当前环境
	public static String getActiveProfile() {
		String active=null;
		String profiles[]= beanFactory.getBean(Environment.class).getActiveProfiles();
		if (null!=profiles && profiles.length>0){
			active=profiles[0];
		}
		return active;
	}

	public static String getPropertyByKey(String key){
		return beanFactory.getBean(Environment.class).getProperty(key);
	}
}
