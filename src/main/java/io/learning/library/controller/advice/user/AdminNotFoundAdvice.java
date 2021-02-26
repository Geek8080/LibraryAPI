package io.learning.library.controller.advice.user;

import io.learning.library.exception.entities.user.AdminNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AdminNotFoundAdvice {

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String AdminNotFoundExceptionHandler(AdminNotFoundException adminNotFoundException){
        return adminNotFoundException.getMessage();
    }
}
