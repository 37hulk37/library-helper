package com.hulk.library.service.impl;

import com.hulk.library.dao.BookRepository;
import com.hulk.library.dao.EventRepository;
import com.hulk.library.dao.ReaderRepository;
import com.hulk.library.dto.EventInfo;
import com.hulk.library.entity.Book;
import com.hulk.library.entity.Event;
import com.hulk.library.entity.Reader;
import com.hulk.library.exception.BadRequestException;
import com.hulk.library.exception.NotFoundException;
import com.hulk.library.request.BorrowBookRequest;
import com.hulk.library.service.EventService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public EventInfo takeBook(BorrowBookRequest request) {
        var reader = findReaderById(request.getReaderId());
        var book = fidBookById(request.getBookId());

        var now  = Date.from(Instant.now());

        var event = eventRepository.save(new Event(
                reader,
                book,
                now,
                DateUtils.addDays(now, request.getUntil())
        ));

        return new EventInfo(event);
    }

    @Override
    @Transactional
    public EventInfo handInBook(Long id) {
        if ( !eventRepository.existsById(id)) {
            throw new BadRequestException("Event with name " + id + " not exists");
        }

        var event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));
        event.setReturned(true);
        eventRepository.save(event);

        return new EventInfo(event);
    }

    @Override
    public List<EventInfo> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(EventInfo::new)
                .toList();
    }

    @Override
    public Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        if ( !eventRepository.existsById(id)) {
            throw new NotFoundException("Event with id " + id + " not exists");
        }

        eventRepository.deleteById(id);
    }

    private Reader findReaderById(Long readerId) {
        return readerRepository.findById(readerId)
                .orElseThrow(() -> new NotFoundException("Reader with id " + readerId + " not found"));
    }

    private Book fidBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book with id " + bookId + " not found"));
    }
}
