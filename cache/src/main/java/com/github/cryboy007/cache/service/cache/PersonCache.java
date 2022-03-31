package com.github.cryboy007.cache.service.cache;

import java.util.function.Function;
import java.util.function.Predicate;

import com.github.cryboy007.cache.model.*;
import com.github.cryboy007.cache.service.PersonService;
import com.github.cryboy007.cache.service.annotation.Cache;
import com.github.cryboy007.cache.service.common.CacheConditionBuilder;
import com.github.cryboy007.cache.service.common.E3Function;
import com.github.cryboy007.cache.service.common.QueryConditionBuilder;
import com.github.cryboy007.cache.service.common.impl.BaseCacheServiceImpl;
import com.github.cryboy007.cache.service.dao.PersonDao;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @ClassName PersonCache
 * @Author tao.he
 * @Since 2022/3/29 17:05
 */
@Service
@Cache(PersonCache.class)
public class PersonCache extends BaseCacheServiceImpl<PersonDao, Person, PersonReq, PersonResp, PersonReqQuery>
        implements PersonService {

    @Override
    protected void setIdCacheCondition(CacheConditionBuilder<PersonReqQuery, Person> builder) {
        Function<PersonReqQuery, Long> getValFun = PersonReqQuery::getId;
        Predicate<Person> predicate = item -> item.getId().equals(getValFun.apply(builder.getR()));
        builder.eq(getValFun,predicate);
    }

    @Override
    protected E3Function<Person, Long> getIdFun() {
        return Person::getId;
    }

    @Override
    protected void setQueryConditions(QueryConditionBuilder<PersonReqQuery, Person> builder) {
        builder.singleLikeMultiIn(PersonReqQuery::getName, Person::getName)
                .exists(Person.class, Person::getName,Person::getName,PersonReqQuery::getName,Person::getName);
    }

    @Override
    protected void setCacheConditions(CacheConditionBuilder<PersonReqQuery, Person> builder) {
        Function<PersonReqQuery, String> getValFun = PersonReqQuery::getName;
        Predicate<Person> predicate = item -> item.getName().equals(getValFun.apply(builder.getR()));
        builder.eq(getValFun,predicate);
    }



    /**
     * 无法在父类初始化缓存,可能是因为不是代理对象
     * @PostConstruct
     *     public void initCache() {
     *         //存放数据
     *         cache.put(this.getClass(), getData());
     *     }
     */
    @PostConstruct
    public void initCache() {
              //存放数据
          cache.put(PersonCache.class.getName(), getData());
    }
}
