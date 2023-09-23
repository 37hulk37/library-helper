CREATE TABLE IF NOT EXISTS books(
    id bigint NOT NULL,
    name varchar(64) UNIQUE NOT NULL,
    author varchar(64) NOT NULL,
    topic varchar(16) NOT NULL,
    description varchar(64) NOT NULL,

    CONSTRAINT books_pk PRIMARY KEY(id)
);