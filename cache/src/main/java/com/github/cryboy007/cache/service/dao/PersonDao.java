package com.github.cryboy007.cache.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.cryboy007.cache.model.Person;
import org.apache.ibatis.annotations.Mapper;

/**
 * @InterfaceName PersonDao
 * @Author HETAO
 * @Date 2022/3/29 18:03
 */
@Mapper
public interface PersonDao extends BaseMapper<Person> {
}
