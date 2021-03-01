package io.learning.library.controller.advice;

import io.learning.library.exception.BookNotAvailableException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BookNotAvailableAdvice {
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    String bootNotAvailableExceptionHandler(BookNotAvailableException bookNotAvailableException){
        return bookNotAvailableException.getMessage();
    }
}
