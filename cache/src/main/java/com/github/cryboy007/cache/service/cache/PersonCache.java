package com.github.cryboy007.cache.service.cache;

import com.github.cryboy007.cache.model.*;
import com.github.cryboy007.cache.service.PersonService;
import com.github.cryboy007.cache.service.annotation.Cache;
import com.github.cryboy007.cache.service.common.E3Function;
import com.github.cryboy007.cache.service.common.QueryConditionBuilder;
import com.github.cryboy007.cache.service.cache.impl.BaseCacheServiceImpl;
import com.github.cryboy007.cache.service.dao.PersonDao;
import com.google.common.cache.LoadingCache;

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
    protected E3Function<Person, Long> getIdFun() {
        return Person::getId;
    }

    @Override
    protected void setQueryConditions(QueryConditionBuilder<PersonReqQuery, Person> builder) {
        builder.singleLikeMultiIn(PersonReqQuery::getName, Person::getName)
                .exists(Person.class, Person::getName,Person::getName,PersonReqQuery::getName,Person::getName);
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
