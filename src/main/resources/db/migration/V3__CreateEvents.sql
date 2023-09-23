CREATE TABLE IF NOT EXISTS events(
    id bigint NOT NULL,
    reader_id bigint NOT NULL,
    book_id bigint NOT NULL,
    "from_d" date DEFAULT CURRENT_DATE NOT NULL,
    until_d date DEFAULT NULL,
    is_returned boolean DEFAULT false NOT NULL,

    CONSTRAINT events_reader_fk FOREIGN KEY (reader_id)
        REFERENCES readers(id),
    CONSTRAINT events_book_fk FOREIGN KEY (book_id)
        REFERENCES books(id),

    CONSTRAINT events_pk PRIMARY KEY(id)
);