package com.hulk.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e) {
        var apiException = new ApiException(e.getMessage(),
                HttpStatus.BAD_REQUEST,
                Date.from(Instant.now())
        );

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        var apiException = new ApiException(e.getMessage(),
                HttpStatus.NOT_FOUND,
                Date.from(Instant.now())
        );

        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }
}
