package com.hulk.library.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "reader_id")
    private Reader reader;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "from_d")
    @Temporal(TemporalType.DATE)
    private Date from;

    @Column(name = "until_d")
    @Temporal(TemporalType.DATE)
    private Date until;

    @Column(name = "is_returned")
    private boolean isReturned;

    public Event(Reader reader, Book book, Date from, Date until) {
        this.reader = reader;
        this.book = book;
        this.from = from;
        this.until = until;
    }
}
