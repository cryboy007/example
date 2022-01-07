package com.tao.mapstruct.Vo;

import com.tao.mapstruct.enums.UserTypeEnum;
import lombok.Data;

/**
 * @ClassName UserEnum
 * @Author tao.he
 * @Since 2022/1/8 0:05
 */
@Data
public class UserEnum {
    private Integer id;
    private String name;
    private String createTime;
    private UserTypeEnum userEnum;
}
