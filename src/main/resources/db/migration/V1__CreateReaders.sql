CREATE TABLE IF NOT EXISTS readers (
    id bigint NOT NULL,
    name varchar(64) UNIQUE NOT NULL,

    CONSTRAINT readers_pk PRIMARY KEY(id)
);