package com.github.cryboy007.cache.service.cache;

import com.github.cryboy007.cache.model.Person;
import com.github.cryboy007.cache.model.PersonReq;
import com.github.cryboy007.cache.model.PersonReqQuery;
import com.github.cryboy007.cache.model.PersonResp;
import com.github.cryboy007.cache.service.PersonService;
import com.github.cryboy007.cache.service.common.E3Function;
import com.github.cryboy007.cache.service.common.QueryConditionBuilder;
import com.github.cryboy007.cache.service.common.impl.BaseCacheServiceImpl;
import com.github.cryboy007.cache.service.dao.PersonDao;
import org.apache.ibatis.annotations.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.PostConstruct;

/**
 * @ClassName PersonCache
 * @Author tao.he
 * @Since 2022/3/29 17:05
 */
@Service
public class PersonCache extends BaseCacheServiceImpl<PersonDao, Person, PersonReq, PersonResp, PersonReqQuery>
        implements PersonService {

    @Override
    protected E3Function<Person, Long> getIdFun() {
        return Person::getId;
    }

    @Override
    protected void setQueryConditions(QueryConditionBuilder<PersonReqQuery, Person> builder) {
        builder.singleLikeMultiIn(PersonReqQuery::getName, Person::getName)
                .eq(PersonReqQuery::getId, Person::getId);
    }

    /**
     * 无法在父类初始化缓存,可能是因为不是代理对象
     * @PostConstruct
     *     public void initCache() {
     *         //存放数据
     *         cache.put(this.getClass(), getData());
     *     }
     */
}