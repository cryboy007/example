package com.github.cryboy007.cache.controller;

import com.github.cryboy007.cache.model.PersonReqQuery;
import com.github.cryboy007.cache.service.cache.PersonCache;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName CacheController
 * @Author tao.he
 * @Since 2022/3/31 15:51
 */
@RestController
@RequiredArgsConstructor
public class CacheController {
    private final PersonCache personCache;

    @GetMapping("cache")
    public void getCache() {
        PersonReqQuery query = new PersonReqQuery();
        query.setName("张三");
        personCache.get(query);
    }
}
