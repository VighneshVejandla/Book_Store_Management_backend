package com.cts.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class ProfileExceptions extends RuntimeException{

    public ProfileExceptions(String msg){
        super(msg);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public static class ProfileAlreadyExistsException extends ProfileExceptions{
        public ProfileAlreadyExistsException(String msg){
            super(msg);
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public static class ProfileIsInActiveException extends ProfileExceptions{
        public ProfileIsInActiveException(String msg){
            super(msg);
        }
    }

}
