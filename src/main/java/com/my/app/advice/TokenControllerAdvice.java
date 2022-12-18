package com.my.app.advice;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.my.app.exception.ErrorMessage;
import com.my.app.exception.TokenRefreshException;
import com.my.app.service.UserService;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class TokenControllerAdvice {
	private final UserService userService;
	 private static final Logger logger = LoggerFactory.getLogger(TokenControllerAdvice.class);
	
	@ExceptionHandler(value = TokenRefreshException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorMessage handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
		// refresh토큰 이상시 db에서 삭제
		userService.removeRefreshToken(ex.getToken());
		logger.info("====================================");
		logger.info("handleTokenRefreshException"); 
		logger.info(ex.getToken());
		logger.info("====================================");
		
		return new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), new Date(), ex.getMessage(),
				request.getDescription(false));
	}
}