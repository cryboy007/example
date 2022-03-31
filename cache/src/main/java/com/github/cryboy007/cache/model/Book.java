package com.github.cryboy007.cache.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.javafaker.Faker;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName Book
 * @Author tao.he
 * @Since 2022/3/30 10:25
 */
@Getter
@Setter
@ToString
@TableName("book")
public class Book {
    @TableId
    private Integer id;
    @TableField("title")
    private String title;
    @TableField(exist = false)
    private String author;

    public static Book create() {
        com.github.javafaker.Book fakerBook = Faker.instance().book();
        Book book = new Book();
        book.setTitle(fakerBook.title());
        book.setAuthor(fakerBook.author());
        return book;
    }
}
