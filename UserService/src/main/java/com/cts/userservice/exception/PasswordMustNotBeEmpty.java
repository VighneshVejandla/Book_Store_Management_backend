package com.cts.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PasswordMustNotBeEmpty extends RuntimeException{
	
	public PasswordMustNotBeEmpty(String msg) {
		super(msg);
	}
}
