package com.cts.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResoureAlreadyExistException extends RuntimeException{
    private String resourceName;
    private String fieldName;
    private Long fieldValue;
    private String fieldString;


    public ResoureAlreadyExistException(String resourceName, String fieldName, Long fieldValue){
        super(String.format("%s with  %s %s is already in active", resourceName, fieldName, fieldValue));

        this.resourceName = resourceName;
        this.fieldName=fieldName;
        this.fieldValue=fieldValue;
    }
}
