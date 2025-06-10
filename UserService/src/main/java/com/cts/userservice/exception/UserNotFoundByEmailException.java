package com.cts.userservice.exception;

public class UserNotFoundByEmailException extends ResourceNotFoundException {
    public UserNotFoundByEmailException(String resourceName, String fieldName, String fieldValue) {
        super(resourceName, fieldName, fieldValue);
    }
}