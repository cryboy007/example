package com.github.cryboy007.mapstruct.start;

import com.github.cryboy007.mapstruct.Vo.UserVo3;
import com.github.cryboy007.mapstruct.convert.UserConvert;
import com.github.cryboy007.mapstruct.model.User;

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
