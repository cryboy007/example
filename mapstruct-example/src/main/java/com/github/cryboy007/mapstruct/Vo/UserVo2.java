package com.github.cryboy007.mapstruct.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName UserVo2
 * @Author tao.he
 * @Since 2022/1/7 23:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVo2 {
    private Integer id;
    private String name;
    private String createTime;
}
