package com.cts.orderservice.exception;

public class PaymentStatusException extends RuntimeException{
	private static final long serialVersionUID = 4L;

	public PaymentStatusException(String msg) {
		super(msg);
	}
}
