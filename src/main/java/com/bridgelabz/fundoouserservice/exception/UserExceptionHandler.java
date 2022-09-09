package com.bridgelabz.fundoouserservice.exception;


import com.bridgelabz.fundoouserservice.util.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Response> handleHiringException(UserNotFoundException he) {
        Response response = new Response();
        response.setErrorCode(400L);
        response.setMessage(he.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
