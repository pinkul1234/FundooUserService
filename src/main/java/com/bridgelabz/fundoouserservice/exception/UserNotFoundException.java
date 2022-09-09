package com.bridgelabz.fundoouserservice.exception;

import com.bridgelabz.fundoouserservice.util.Response;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus
public class UserNotFoundException extends RuntimeException{
    public Response getErrorResponse;
    private int statusCode;
    private String statusMessage;
    public UserNotFoundException(int statusCode, String statusMessage) {
        super(statusMessage);
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}

