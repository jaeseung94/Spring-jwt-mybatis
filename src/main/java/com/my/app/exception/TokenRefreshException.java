package com.my.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  
  private String token;

  public TokenRefreshException(String token, String message) {
    super(String.format("Failed for [%s]: %s", token, message));
    this.token = token;
  }

	public String getToken() {
		return token;
	}
	
}