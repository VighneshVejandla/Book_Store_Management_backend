package com.cts.userservice.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(UserNotFoundByIdException.class)
	public ResponseEntity<ErrorDetails> handleUserNotFoundByIdException(UserNotFoundByIdException exception, WebRequest webRequest){
		
		ErrorDetails errorDetails = new ErrorDetails(
				LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(true),
				"USER_NOT_FOUND"
				);
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNotFoundByEmailException.class)
	public ResponseEntity<ErrorDetails> handelUserNotFoundByEmailException(UserNotFoundByEmailException exception, WebRequest webRequest){

		ErrorDetails errorDetails = new ErrorDetails(
				LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(true),
				"EMAIL_NOT_FOUND"
		);
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
	
	 
	@ExceptionHandler(UserNameAlreadyExistsException.class)
	public ResponseEntity<ErrorDetails> handleUserAlreadyExistsException(UserNameAlreadyExistsException exception, WebRequest webRequest){
		
		ErrorDetails errorDetails = new ErrorDetails(
				LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(false),
				"USER_NAME_ALREADY_EXISTS"
				);
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<ErrorDetails> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception, WebRequest webRequest){
		
		ErrorDetails errorDetails = new ErrorDetails(
				LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(false),
				"EMAIL_ALREADY_EXISTS"
				);
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(PasswordMustNotBeEmpty.class)
	public ResponseEntity<ErrorDetails> handlePasswordMustNotBeEmpty(PasswordMustNotBeEmpty exception, WebRequest webRequest){
		
		ErrorDetails errorDetails = new ErrorDetails(
				LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(false),
				"PASSWORD_ERROR"
				);
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(TransactionSystemException.class)
	public ResponseEntity<String> handleTransactionSystemException(TransactionSystemException ex) {
		Throwable cause = ex.getRootCause();
		if (cause instanceof ConstraintViolationException) {
			ConstraintViolationException violationException = (ConstraintViolationException) cause;
			String errors = violationException.getConstraintViolations()
					.stream()
					.map(ConstraintViolation::getMessage)
					.collect(Collectors.joining(", "));
			return new ResponseEntity<>("Validation failed: " + errors, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Transaction error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> Exception(Exception exception, WebRequest webRequest){
		
		ErrorDetails errorDetails = new ErrorDetails(
				LocalDateTime.now(),
				exception.getClass().getName(),
				webRequest.getDescription(false),
				"INTERNAL_SERVER_ERROR"
				);
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	// In your GlobalExceptionHandler.java
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
//		System.err.println("DEBUG: Inside handleResourceNotFoundException. Message: " + ex.getMessage());
		ErrorDetails errorDetails = new ErrorDetails(
				LocalDateTime.now(),
				ex.getMessage() != null && !ex.getMessage().isEmpty() ? ex.getMessage() : "Resource not found (No specific message)",
				request.getDescription(false),
				"RESOURCE_NOT_FOUND"
		);
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}


	@ExceptionHandler(ProfileExceptions.ProfileAlreadyExistsException.class)
	public ResponseEntity<ErrorDetails> handleProfileAlreadyExistsException(ProfileExceptions.ProfileAlreadyExistsException ex, WebRequest request){
		ErrorDetails errorDetails = new ErrorDetails(
				LocalDateTime.now(),
				ex.getMessage(),
				request.getDescription(false),
				"PROFILE_ALREADY_EXISTS"
		);
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler(ProfileExceptions.ProfileIsInActiveException.class)
	public ResponseEntity<ErrorDetails> handleProfileIsInActiveException(ProfileExceptions.ProfileIsInActiveException ex, WebRequest request){
		ErrorDetails errorDetails = new ErrorDetails(
				LocalDateTime.now(),
				ex.getMessage(),
				request.getDescription(false),
		"PROFILE_ALREADY_IN_ACTIVE"
		);
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	
//	----------Exception for the service--------------
	
	 @ExceptionHandler(ConstraintViolationException.class)
	    public ResponseEntity<Object> handleConstraintViolationException(
	            ConstraintViolationException ex, WebRequest request) {

	        // Collect all constraint violations into a map (property path -> error message)
	        Map<String, String> errors = ex.getConstraintViolations().stream()
	                .collect(Collectors.toMap(
	                        // Extract the field name from the property path (e.g., "getUser.id" -> "id")
	                        violation -> {
	                            String path = violation.getPropertyPath().toString();
	                            int lastDotIndex = path.lastIndexOf('.');
	                            return lastDotIndex != -1 ? path.substring(lastDotIndex + 1) : path;
	                        },
	                        ConstraintViolation::getMessage // Value: the error message for the violation
	                ));

	        // Return a ResponseEntity with the errors map and a 400 Bad Request status
	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	    }
	
	 
	 @ExceptionHandler(DataIntegrityViolationException.class)
	    public ResponseEntity<Object> handleDataIntegrityViolationException(
	            DataIntegrityViolationException ex, WebRequest request) {

	        Map<String, String> errors = new HashMap<>();
	        String errorMessage = "A conflict occurred with existing data. Please check your input.";

	        if (ex.getRootCause() != null && ex.getRootCause().getMessage() != null &&
	            ex.getRootCause().getMessage().toLowerCase().contains("duplicate entry")) {
	            errorMessage = "This record already exists. Please provide unique information.";
	        }

	        errors.put("message", errorMessage); // Using "message" or "error" as the key

	        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
	    }
	 
	 
	 
//	------------Exception for the Controller------------
	 
	 
//	@Override
//	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//			
//		Map<String, String> errors = new HashMap<>();
//		
//		List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
//		
//		errorList.forEach((error) -> {
//			String fieldName = ((FieldError) error).getField();
//			String message = error.getDefaultMessage();
//			errors.put(fieldName, message);
//		});
//		
//		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//	}
	
	
}
