package com.hulk.library.service;

import com.hulk.library.dto.BookInfo;
import com.hulk.library.entity.Book;
import com.hulk.library.request.NewBookRequest;

import java.util.Date;

public interface BookService {
    BookInfo createBook(NewBookRequest request);

    BookInfo updateBook(Book request);

    BookInfo getMostPopularBook(Date from, Date until);

    BookInfo getBook(Long id);

    void deleteBook(Long id);
}
