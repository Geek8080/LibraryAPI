package io.learning.library.controller.advice;

import io.learning.library.exception.entities.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BookNotFoundAdvice {

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String BookNotFoundExceptionHandler(BookNotFoundException bookNotFoundException){
        return bookNotFoundException.getMessage();
    }
}
