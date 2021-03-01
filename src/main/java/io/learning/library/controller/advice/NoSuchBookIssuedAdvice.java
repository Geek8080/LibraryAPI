package io.learning.library.controller.advice;

import io.learning.library.exception.NoSuchBookIssuedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NoSuchBookIssuedAdvice {
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    String noSuchBookIssuedExceptionHandler(NoSuchBookIssuedException noSuchBookIssuedException){
        return noSuchBookIssuedException.getMessage();
    }
}
