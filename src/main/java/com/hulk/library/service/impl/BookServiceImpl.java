package com.hulk.library.service.impl;

import com.hulk.library.dao.BookRepository;
import com.hulk.library.dto.BookInfo;
import com.hulk.library.entity.Book;
import com.hulk.library.exception.NotFoundException;
import com.hulk.library.utils.request.NewBookRequest;
import com.hulk.library.service.BookService;
import com.hulk.library.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository repository;

    @Override
    @Transactional
    public BookInfo createBook(NewBookRequest request) {
        if (repository.existsBookByName(request.getName())) {
            throw new BadRequestException("Book with name " + request.getName() + " already exists");
        }

        var book = repository.save(new Book(
                request.getName(),
                request.getAuthor(),
                request.getTopic(),
                request.getDescription()
        ));

        return new BookInfo(book);
    }

    @Override
    @Transactional
    public BookInfo updateBook(Book request) {
        if (repository.existsBookByName(request.getName()) &&
                !request.getName().equals(repository.getBookNameById(request.getId()))) {
            throw new BadRequestException("Book with name " + request.getName() + " already exists");
        }

        if (!repository.existsById(request.getId())) {
            throw new NotFoundException("Book with id " + request.getId() + " not found");
        }

        repository.save(request);

        return new BookInfo(request);
    }

    @Override
    public BookInfo getMostPopularBook(Date from, Date until) {
        var book = repository.findMostPopularBook(from, until)
                .orElseThrow(() -> new NotFoundException("No one read books from " + from + " until " + until));

        return new BookInfo(book);
    }

    @Override
    public BookInfo getBook(Long id) {
        var book = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id " + id + "not found"));

        return new BookInfo(book);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        if ( !repository.existsById(id)) {
            throw new NotFoundException("Book with id " + id + "not exists");
        }

        repository.deleteById(id);
    }
}
