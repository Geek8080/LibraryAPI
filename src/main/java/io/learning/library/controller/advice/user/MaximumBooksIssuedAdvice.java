package io.learning.library.controller.advice.user;

import io.learning.library.exception.user.MaximumBooksIssuedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MaximumBooksIssuedAdvice {
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String maximumBooksIssuedExceptionHandler(MaximumBooksIssuedException maximumBooksIssuedException){
        return maximumBooksIssuedException.getMessage();
    }
}
