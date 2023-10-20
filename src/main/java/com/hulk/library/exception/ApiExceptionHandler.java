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
        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                Date.from(Instant.now()),
                null
        );

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(
            NotFoundException e) {
        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.NOT_FOUND,
                Date.from(Instant.now()),
                null
        );

        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ApplicationException.class})
    public ResponseEntity<Object> handleApplicationException(ApplicationException e) {
        var parsedExceptionMessage = getParsedExceptionMessage(e.getMessage());

        var apiException = new ApiException(
                getLocalisedMessage(parsedExceptionMessage.code, parsedExceptionMessage.params),
                HttpStatus.BAD_REQUEST,
                Date.from(Instant.now()),
                Integer.parseInt(parsedExceptionMessage.code)
        );

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    private ParsedExceptionMessage getParsedExceptionMessage(String message) {
        var str = message.split("/");

        return new ParsedExceptionMessage(str[0], (str.length > 1 ? getParameters(str[1]) : null));
    }

    private String[] getParameters(String params) {
        return params.substring(1, params.length()-1).split(",");
    }

    private String getLocalisedMessage(String code, Object[] params) {
        return messageSource.getMessage("error.code." + code, params, "Something went wrong", Locale.getDefault());
    }

    private record ParsedExceptionMessage (
            String code,
            Object[] params
    ) {}

}
