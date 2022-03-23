package com.github.cryboy007.mapstruct.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @ClassName ItemVo
 * @Author tao.he
 * @Since 2022/1/7 23:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVo {
    private Integer id;
    private String name;
    private String createTime;
    private LocalDateTime updateTime;
}
