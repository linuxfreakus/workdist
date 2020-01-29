package com.example.workdist.api.errors;

import com.example.workdist.api.message.ResponseMsg;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.InvalidParameterException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseMsg handleWorkItemAssignmentException(final NotFoundException ex) {
        ResponseMsg responseMsg = new ResponseMsg(ex.getMessage());
        return responseMsg;
    }

    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMsg handleNotFoundException(InvalidParameterException ex) {
        ResponseMsg responseMsg = new ResponseMsg(ex.getMessage());
        return responseMsg;
    }

}