package com.github.cryboy007.cache.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Person
 * @Author tao.he
 * @Since 2022/3/29 16:26
 */
@Data
@TableName("person")
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @TableId(type = IdType.INPUT)
    private Long id;

    @TableField("name")
    private String name;
}
