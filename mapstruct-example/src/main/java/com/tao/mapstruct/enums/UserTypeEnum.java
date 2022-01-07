package com.tao.mapstruct.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @enumName UserEnum
 * @Author HETAO
 * @Date 2022/1/8 0:01
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {
    Java("000", "Java开发工程师"),
    DB("001", "数据库管理员"),
    LINUX("002", "Linux运维员");

    private String value;
    private String title;
}
