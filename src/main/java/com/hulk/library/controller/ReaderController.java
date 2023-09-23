package com.hulk.library.controller;

import com.hulk.library.dto.ReaderInfo;
import com.hulk.library.entity.Reader;
import com.hulk.library.request.NewReaderRequest;
import com.hulk.library.service.ReaderService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;


@RestController
@RequestMapping("/api/readers")
@AllArgsConstructor
public class ReaderController {
    private final ReaderService readerService;
    private final static Logger logger = LoggerFactory.getLogger(BookController.class);

    @PostMapping
    public ReaderInfo createReader(@RequestBody NewReaderRequest request) {
        logger.info("Reader was created with name " + request.getName());
        return readerService.createReader(request);
    }

    @PutMapping
    public ReaderInfo updateReader(@RequestBody Reader request) {
        logger.info("Reader with name " + request.getName() + " was updated");
        return readerService.updateReader(request);
    }

    @GetMapping
    public ReaderInfo getBestReader(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date until
    ) {
        logger.info("Retrieved most popular book");
        return readerService.getBestReader(from, until);
    }

    @GetMapping("/{id}")
    public ReaderInfo getReader(@PathVariable Long id) {
        return readerService.getReader(id);
    }

    @DeleteMapping("/{id}")
    public void deleteReader(@PathVariable Long id) {
        logger.info("Reader with id " + id + " was deleted");
        readerService.deleteReader(id);
    }
}