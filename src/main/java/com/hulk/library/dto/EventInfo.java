package com.hulk.library.dto;

import com.hulk.library.entity.Event;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class EventInfo {
    private Long id;
    private Long readerId;
    private Long bookId;
    private Date from;
    private Date until;
    private boolean isReturned;

    public EventInfo(Event event) {
        this.id = event.getId();
        this.readerId = event.getReader().getId();
        this.bookId = event.getBook().getId();
        this.from = event.getFrom();
        this.until = event.getUntil();
        this.isReturned = event.isReturned();
    }
}
