//package com.github.cryboy007.cache.service.cache;
//
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//import com.github.cryboy007.cache.service.cache.impl.BaseCacheServiceImpl;
//import com.github.cryboy007.utils.SpringContextUtil;
//import com.google.common.cache.CacheLoader;
//import com.google.common.util.concurrent.ListenableFuture;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @ClassName MyCacheLoader
// * @Author tao.he
// * @Since 2022/3/29 17:26
// */
//@Slf4j
//public class MyCacheLoader extends CacheLoader<String,String> {
//    @Override
//    public String load(String key) throws Exception {
//        BaseCacheServiceImpl baseMapper = (BaseCacheServiceImpl) SpringContextUtil.getBeanByClass(Class.forName(key));
//        log.info("loadingData");
//         try {
//                 TimeUnit.SECONDS.sleep(30);
//             } catch (InterruptedException e) {
//                 e.printStackTrace();
//         }
//        return baseMapper.getData();
//    }
//
//    @Override
//    public ListenableFuture<String> reload(String key, String oldValue) throws Exception {
//        return super.reload(key, oldValue);
//    }
//}
