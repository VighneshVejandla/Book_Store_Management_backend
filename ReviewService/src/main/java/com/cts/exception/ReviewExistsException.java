package com.cts.exception;

public class ReviewExistsException extends RuntimeException{
    public ReviewExistsException(String msg){
        super(msg);
    }
}
