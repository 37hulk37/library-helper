package com.hulk.library.controller;

import com.hulk.library.dto.BookInfo;
import com.hulk.library.entity.Book;
import com.hulk.library.utils.request.NewBookRequest;
import com.hulk.library.service.BookService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {
    private final BookService bookService;
    private final static Logger logger = LoggerFactory.getLogger(BookController.class);

    @PostMapping
    public BookInfo createBook(@RequestBody NewBookRequest request) {
        logger.info("Book was created with name " + request.getName());
        return bookService.createBook(request);
    }

    @PutMapping
    public BookInfo updateBook(@RequestBody Book request) {
        logger.info("Book with name " + request.getName() + " was updated");
        return bookService.updateBook(request);
    }

    @GetMapping
    public BookInfo getMostPopularBook(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date until
    ) {
        logger.info("Retrieved most popular book");
        return bookService.getMostPopularBook(from, until);
    }

    @GetMapping("/{id}")
    public BookInfo getBook(@PathVariable Long id) {
        return bookService.getBook(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        logger.info("Book with id " + id + " was deleted");
        bookService.deleteBook(id);
    }
}
