package com.github.cryboy007.cache.config;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.cryboy007.cache.service.cache.impl.BaseCacheServiceImpl;
import com.github.cryboy007.utils.SpringContextUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *@ClassName CacheConfig
 *@Author tao.he
 *@Since 2022/3/31 20:21
 */
@Configuration
@Slf4j
public class CacheConfig {

	@Bean
	public <T> LoadingCache<String,List<T>> guavaCache() {
		return CacheBuilder.newBuilder()
				.maximumSize(1000)
				.expireAfterWrite(10, TimeUnit.MINUTES)
				.refreshAfterWrite(2, TimeUnit.MINUTES)
				.recordStats()
				.build(new CacheLoader<String, List<T>>() {
					@Override
					public List<T> load(String key) throws Exception {
						BaseCacheServiceImpl baseMapper = (BaseCacheServiceImpl) SpringContextUtil.getBeanByClass(Class.forName(key));
						log.info("loadingData");
						try {
							TimeUnit.SECONDS.sleep(30);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return baseMapper.getData();
					}
				});
	}
}
