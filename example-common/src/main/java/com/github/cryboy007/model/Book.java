package com.github.cryboy007.model;

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
public class Book {
    private String title;
    private String author;

    public static Book create() {
        com.github.javafaker.Book fakerBook = Faker.instance().book();
        Book book = new Book();
        book.setTitle(fakerBook.title());
        book.setAuthor(fakerBook.author());
        return book;
    }
}
