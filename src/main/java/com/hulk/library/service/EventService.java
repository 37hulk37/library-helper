package com.hulk.library.service;

import com.hulk.library.dto.EventInfo;
import com.hulk.library.entity.Event;
import com.hulk.library.utils.request.BorrowBookRequest;

import java.util.List;

public interface EventService {
    EventInfo takeBook(BorrowBookRequest request);

    EventInfo returnBook(Long id);

    List<EventInfo> getAllEvents();

    Event getEvent(Long id);

    void deleteEvent(Long id);
}
