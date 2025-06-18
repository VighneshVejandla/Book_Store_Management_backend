package com.cts.exception;

public class TransactionTimeException extends RuntimeException{
    public TransactionTimeException(String message){
        super(message);
    }
}
