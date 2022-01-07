package com.tao.mapstruct.start;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.tao.mapstruct.Vo.UserVo;
import com.tao.mapstruct.Vo.UserVo3;
import com.tao.mapstruct.convert.UserConvert;
import com.tao.mapstruct.model.User;
import org.mapstruct.ap.shaded.freemarker.template.utility.DateUtil;
import org.mapstruct.factory.Mappers;

/**
 * @ClassName ConvertStartMain
 * @Author tao.he
 * @Since 2022/1/7 23:12
 */
public class ConvertStartMain {

    public static void main(String[] args) {
        UserConvert convert =UserConvert.INSTANCE;
        User user = User.builder().id(1).name("张三").createTime("2018-01-12 17:07:05").build();

        UserVo3 vo = convert.toConvertVO3(user);
        System.out.println(vo);
    }
}
