package com.github.cryboy007.cache.controller;

import com.apifan.common.random.source.NumberSource;
import com.github.cryboy007.cache.model.Person;
import com.github.cryboy007.cache.model.PersonReqQuery;
import com.github.cryboy007.cache.service.PersonService;
import com.github.cryboy007.cache.service.cache.PersonCache;
import com.github.cryboy007.model.Book;
import com.github.javafaker.Faker;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName CacheController
 * @Author tao.he
 * @Since 2022/3/31 15:51
 */
@RestController
@RequiredArgsConstructor
@EnableAsync
@EnableScheduling
public class CacheController {
    private final PersonService personCache;

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

    @PostMapping("find")
    public ResponseEntity<List> find(@RequestBody PersonReqQuery query) {

        return ResponseEntity.ok(personCache.useCache(true).find(query));
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Long id,@RequestParam(value = "useCache",required = false) boolean useCache) {
        return ResponseEntity.ok(personCache.useCache(useCache).get(id));
    }

    @GetMapping("batchSave")
    public ResponseEntity<Object> batchSave() {
        Faker faker = Faker.instance(Locale.CHINA);
        List<Person> personList = Stream.generate(() -> new Person(null, faker.name().fullName())).limit(10000)
                .collect(Collectors.toList());
        //插入假数据
        List<List<Person>> partition = Lists.partition(personList, 1000);
        partition.forEach(personCache::saveBatch);
        return ResponseEntity.ok().build();
    }

    @PostMapping("list")
    public ResponseEntity<List> list(@RequestBody List<Long> ids,@RequestParam("useCache") boolean useCache) {
        PersonReqQuery query = new PersonReqQuery();
        //query.setName("张三,李武");
        query.setName("张");
        return ResponseEntity.ok(personCache.useCache(useCache).find(query));
    }
}
