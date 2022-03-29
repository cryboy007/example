package com.github.cryboy007.cache.service;

import com.github.cryboy007.cache.model.Person;
import com.github.cryboy007.cache.model.PersonReq;
import com.github.cryboy007.cache.model.PersonReqQuery;
import com.github.cryboy007.cache.model.PersonResp;
import com.github.cryboy007.cache.service.common.IBaseCacheService;
import com.github.cryboy007.cache.service.dao.PersonDao;

/**
 * @InterfaceName PersonService
 * @Author HETAO
 * @Date 2022/3/29 19:07
 */
public interface PersonService extends  IBaseCacheService<Person, PersonReq, PersonResp, PersonReqQuery>  {
}
