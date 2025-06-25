package com.cts.orderservice.exception;

public class OutOfStockException extends RuntimeException{

	private static final long serialVersionUID = 2L;

	public OutOfStockException(String msg) {
		super(msg);
	}
}
