package com.cts.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleProductNotFoundException(ProductNotFoundException ex) {
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("error", ex.getMessage()); // ✅ Custom message instead of default 404

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(CartNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleCartNotFoundException(CartNotFoundException ex) {
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("error", "Cart not found");

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
	    String errorMessage = ex.getBindingResult().getFieldErrors().stream()
	            .map(error -> error.getField() + ": " + error.getDefaultMessage())
	            .collect(Collectors.joining(", "));
	    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}


@ExceptionHandler(UserNotFoundException.class)
public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
	return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
}

@ExceptionHandler(OutOfStockException.class)
public ResponseEntity<String> handleOutOfStock( OutOfStockException ex) {
	return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
}

	@ExceptionHandler(CartUpdateException.class)
	public ResponseEntity<String> handleCartUpdateException(CartUpdateException ex) {
		System.out.println(ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Error updating cart: " + ex.getMessage());
	}
}
