package com.hulk.library.exception;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Date;
import java.util.Locale;

@ControllerAdvice
@AllArgsConstructor
public class ApiExceptionHandler {
    private final MessageSource messageSource;

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

    @ExceptionHandler(value = {ApplicationException.class})
    public ResponseEntity<Object> handleApplicationException(ApplicationException e) {
        var apiException = new ApiException(getMessage(e.getMessage(), e.getParams()),
                HttpStatus.BAD_REQUEST,
                Date.from(Instant.now())
        );

        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    private String getMessage(String code, Object[] params) {
        return messageSource.getMessage("error.code." + code, params, "Something went wrong", Locale.getDefault());
    }

}
