package com.cts.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ErrorMessage {
	private String message;
	private HttpStatus httpstatus;
}
