package com.cts.userservice.exception;

public class UserNotFoundByIdException extends ResourceNotFoundException {
    public UserNotFoundByIdException(String resourceName, String fieldName, Long fieldValue) {
        super(resourceName, fieldName, fieldValue);
    }
}