package com.github.cryboy007.mapstruct.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @ClassName UserVo3
 * @Author tao.he
 * @Since 2022/1/7 23:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVo3 {
    private String id;
    private String name;
    // 实体类该属性是String
    private LocalDateTime createTime;
    // 实体类该属性是LocalDateTime
    private String updateTime;
}
