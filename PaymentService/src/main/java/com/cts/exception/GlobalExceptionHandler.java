package com.cts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

public class GlobalExceptionHandler {
	@ExceptionHandler(UserIdException.class)
	public ResponseEntity<ErrorMessage> InvalidUserException(UserIdException exc) {
		ErrorMessage errordata = new ErrorMessage();
		errordata.setMessage(exc.getMessage());
		errordata.setHttpstatus(HttpStatus.BAD_REQUEST);
		return new ResponseEntity<ErrorMessage>(errordata, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(PaymentException.class)
	public ResponseEntity<ErrorMessage> PaymentDefaultException(PaymentException pay) {
		ErrorMessage errordata = new ErrorMessage();
		errordata.setMessage(pay.getMessage());
		errordata.setHttpstatus(HttpStatus.BAD_REQUEST);
		return new ResponseEntity<ErrorMessage>(errordata, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AmountException.class)
	public ResponseEntity<ErrorMessage> TransactionAmountException(AmountException amo) {
		ErrorMessage errormessage = new ErrorMessage();
		errormessage.setMessage(amo.getMessage());
		errormessage.setHttpstatus(HttpStatus.BAD_REQUEST);
		return new ResponseEntity<ErrorMessage>(errormessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(StatusChangeException.class)
	public ResponseEntity<ErrorMessage> ChangeStatusException(StatusChangeException st) {
		ErrorMessage errormessage = new ErrorMessage();
		errormessage.setMessage(st.getMessage());
		errormessage.setHttpstatus(HttpStatus.BAD_REQUEST);
		return new ResponseEntity<ErrorMessage>(errormessage, HttpStatus.BAD_REQUEST);
	}

}
