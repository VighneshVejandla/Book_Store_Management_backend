package com.cts.exception;

import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException unf){
        return new ResponseEntity<>(unf.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> handleBookNotFoundException(BookNotFoundException bnf){
        return new ResponseEntity<>(bnf.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReviewExistsException.class)
    public ResponseEntity<String> handleReviewExists(ReviewExistsException fff){
        return new ResponseEntity<>(fff.getMessage(), HttpStatus.ALREADY_REPORTED);
    }

}
