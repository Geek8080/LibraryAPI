package io.learning.library.controller.advice.user;

import io.learning.library.exception.user.StudentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class StudentNotFoundAdvice {

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String StudentNotFoundExceptionHandler(StudentNotFoundException studentNotFoundException){
        return studentNotFoundException.getMessage();
    }
}
