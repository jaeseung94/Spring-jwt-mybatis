package com.my.app.exception;

public class OAuthProcessingException extends RuntimeException {
	public OAuthProcessingException(String message) {
        super(message);
    }
}
