package io.learning.library.controller.advice;

import io.learning.library.exception.BookAlreadyIssuedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BookAlreadyIssuedAdvice {
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    String bookAlreadyIssuedExceptionHandler(BookAlreadyIssuedException bookAlreadyIssuedException){
        return bookAlreadyIssuedException.getMessage();
    }
}
