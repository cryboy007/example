package com.github.cryboy007.cache.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @ClassName PersonReqQuery
 * @Author tao.he
 * @Since 2022/3/29 19:04
 */
@Data
public class PersonReqQuery {
    private Long id;

    private String name;
}
