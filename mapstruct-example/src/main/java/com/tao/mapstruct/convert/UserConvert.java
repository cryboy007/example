package com.tao.mapstruct.convert;

import com.tao.mapstruct.Vo.*;
import com.tao.mapstruct.enums.UserTypeEnum;
import com.tao.mapstruct.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @ClassName UserConvert
 * @Author tao.he
 * @Since 2022/1/7 23:07
 *
 * default: 这是默认的情况，mapstruct 不使用任何组件类型, 可以通过Mappers.getMapper(Class)方式获取自动生成的实例对象。
 * cdi: the generated mapper is an application-scoped CDI bean and can be retrieved via @Inject
 * spring: 生成的实现类上面会自动添加一个@Component注解，可以通过Spring的 @Autowired方式进行注入
 * jsr330: 生成的实现类上会添加@javax.inject.Named 和@Singleton注解，可以通过 @Inject注解获取
 */
//@Mapper(componentModel = "spring")
//@Mapper(componentModel = "jsr330")
@Mapper
public interface UserConvert {
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    /**
     * 字段数量类型数量相同，利用工具BeanUtils也可以实现类似效果
     * @param source
     * @return
     */
    UserVo toConvertVO1(User source);
    User fromConvertEntity1(UserVo userVo);

    /**
     * 集合转换
     * @param source
     * @return
     */
    List<UserVo> toConvertVO1(List<User> source);
    /**
     * @param source
     * @return
     */
    UserVo2 toConvertVO2(User source);

    /**
     * 类型不一致
     * @param source
     * @return
     */
    @Mappings({
            @Mapping(target = "createTime", expression = "java(cn.hutool.core.date.LocalDateTimeUtil.parse(source.getCreateTime(),\"yyy-MM-dd HH:mm:ss\"))"),
    })
    UserVo3 toConvertVO3(User source);

    User fromConvertEntity3(UserVo3 userVO3);
    /**
     *  字段不一致
     *  @Mappings({
     *             @Mapping(source = "id", target = "userId"),
     *             @Mapping(source = "name", target = "userName")
     *     })
     *     UserVO4 toConvertVO(User source);
     *
     *     User fromConvertEntity(UserVO4 userVO4);
     */
    @Mapping(source = "userEnum", target = "type")
    UserVo4 toConvertVO5(UserEnum source);

    UserEnum fromConvertEntity5(UserVo4 userVo4);
}
