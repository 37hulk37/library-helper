package com.hulk.library.dao;

import com.hulk.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsBookByName(String name);

    @Query("select b from Book b " +
            "join Event e on b.id = e.book.id " +
            "where e.from >= :from " +
                "and e.until <= :until " +
                "and e.isReturned = true " +
            "group by b.id " +
            "order by count(*) desc " +
            "limit 1" )
    Optional<Book> findMostPopularBook(Date from, Date until);
}
