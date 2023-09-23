package com.hulk.library.dto;

import com.hulk.library.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookInfo {
    private Long id;
    private String name;
    private String author;
    private String topic;
    private String description;

    public BookInfo(Book book) {
        this.id = book.getId();
        this.name = book.getName();
        this.author = book.getAuthor();
        this.topic = book.getTopic();
        this.description = book.getDescription();
    }
}
