package com.hulk.library.dao;

import com.hulk.library.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.Optional;

public interface ReaderRepository extends JpaRepository<Reader, Long> {
    boolean existsReaderByName(String name);

    @Query("select r.name from Reader r where r.id = :id")
    String getReaderNameById(Long id);

    @Query("select r from Reader r " +
            "join Event e on r.id = e.book.id " +
            "where e.from >= :from " +
                "and e.until <= :until " +
                "and e.isReturned = true " +
            "group by r.id " +
            "order by count(*) desc " +
            "limit 1" )
    Optional<Reader> findBestReader(Date from, Date until);
}
