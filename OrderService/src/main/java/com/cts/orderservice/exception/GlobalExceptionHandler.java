package com.cts.orderservice.exception;import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(IdNotFoundException.class)
	public ResponseEntity<String> myException(IdNotFoundException exception){
		return new ResponseEntity<String>(exception.getMessage(),HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(OutOfStockException.class)
	public ResponseEntity<String> myException(OutOfStockException exception){
		return new ResponseEntity<String>(exception.getMessage(),HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,String>> myExceptionResponse3(MethodArgumentNotValidException ex) {
		Map<String,String> errors = new HashMap<>();
		for(FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			errors.put(fieldError.getField(),fieldError.getDefaultMessage());
		}
		return new ResponseEntity<Map<String,String>>(errors, HttpStatus.BAD_REQUEST);
	}
}
