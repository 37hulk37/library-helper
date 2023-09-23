package com.hulk.library.controller;

import com.hulk.library.dto.EventInfo;
import com.hulk.library.entity.Event;
import com.hulk.library.request.HandInBookRequest;
import com.hulk.library.request.BorrowBookRequest;
import com.hulk.library.service.EventService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@AllArgsConstructor
public class EventController {
    private final EventService eventService;
    private final static Logger logger = LoggerFactory.getLogger(BookController.class);

    @PostMapping
    public EventInfo takeBook(@RequestBody BorrowBookRequest request) {
        logger.info("Reader with id " + request.getReaderId() + " take book with id " + request.getBookId());
        return eventService.takeBook(request);
    }

    @PutMapping
    public EventInfo updateReader(@RequestBody HandInBookRequest request) {
        logger.info("Reader hand in book, event id " + request.getEventId());
        return eventService.handInBook(request.getEventId());
    }

    @GetMapping
    public List<EventInfo> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public Event getEvent(@PathVariable Long id) {
        return eventService.getEvent(id);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        logger.info("Event with id " + id + " was deleted");
        eventService.deleteEvent(id);
    }
}
