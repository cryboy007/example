package com.github.cryboy007.cache.controller;

import com.github.cryboy007.cache.model.PersonReqQuery;
import com.github.cryboy007.cache.service.cache.PersonCache;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
    public ResponseEntity<List> getCache(@RequestParam(value = "useCache",required = false) boolean useCache) {
        PersonReqQuery query = new PersonReqQuery();
        return ResponseEntity.ok(personCache.useCache(useCache).find(null));
    }

    @RequestMapping("deleteCache/{id}")
    public ResponseEntity<List> deleteCache(@PathVariable("id") Long id) {
        PersonReqQuery query = new PersonReqQuery();
        HashSet<Long> ids = Sets.newHashSet(id);
        personCache.delete(ids);
        return ResponseEntity.ok().build();
    }

    @GetMapping("cacheTwo")
    public ResponseEntity<List> cacheTwo(@RequestParam(value = "useCache",required = false) boolean useCache) {
        PersonReqQuery query = new PersonReqQuery();
        return ResponseEntity.ok(personCache.useCache(useCache).find(null));
    }
}
