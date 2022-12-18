//package com.my.app.handler;
//
//import static org.springframework.http.HttpStatus.CONFLICT;
//import static org.springframework.http.HttpStatus.FORBIDDEN;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import com.my.app.controller.UserController;
//import com.my.app.dto.ErrorDto;
//import com.my.app.exception.DuplicateMemberException;
//import com.my.app.exception.NotFoundMemberException;
//
//@ControllerAdvice
//public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {
//	
//    private static final Logger logger = LoggerFactory.getLogger(RestResponseExceptionHandler.class);
//
//
//    @ResponseStatus(CONFLICT)
//    @ExceptionHandler(value = { DuplicateMemberException.class })
//    @ResponseBody
//    protected ErrorDto badRequest(RuntimeException ex, WebRequest request) {
//    	logger.info("======================================");
//    	logger.info("RestResponseExceptionHandler badRequest()");
//    	logger.info("======================================");
//        return new ErrorDto(CONFLICT.value(), ex.getMessage());
//    }
//
//    @ResponseStatus(FORBIDDEN)
//    @ExceptionHandler(value = { NotFoundMemberException.class, AccessDeniedException.class })
//    @ResponseBody
//    protected ErrorDto forbidden(RuntimeException ex, WebRequest request) {
//    	logger.info("======================================");
//    	logger.info("RestResponseExceptionHandler forbidden()");
//    	logger.info("======================================");
//        return new ErrorDto(FORBIDDEN.value(), ex.getMessage());
//    }
//}