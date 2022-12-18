package com.my.app.jwt;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		// 유효한 자격증명을 제공하지 않고 접근하려 할때 401
		logger.info("=================================================");
		logger.info("유효한 자격증명을 제공하지 않고 접근하려 할때 401 ");
		logger.info("=================================================");
		
		//if(request.getHeader(JwtFilter.AUTHORIZATION_HEADER) )
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

	    final Map<String, Object> body = new HashMap<>();
	    body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
	    body.put("error", "unauthenticated");
	    body.put("message", authException.getMessage());
	    body.put("path", request.getServletPath());

	    final ObjectMapper mapper = new ObjectMapper();
	    mapper.writeValue(response.getOutputStream(), body);
		
//		
//		if(request.getServletPath().equals("/comm/api/login")) {
//			logger.info("=================================================");
//			logger.info("로그인 경로 ");
//			logger.info("=================================================");
//			
//			String errorMessage;
//	        if (authException instanceof BadCredentialsException) {
//	            errorMessage = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";
//	        } else if (authException instanceof InternalAuthenticationServiceException) {
//	            errorMessage = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
//	        } else if (authException instanceof UsernameNotFoundException) {
//	            errorMessage = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
//	        } else if (authException instanceof AuthenticationCredentialsNotFoundException) {
//	            errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
//	        } else {
//	            errorMessage = "알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요.";
//	        }
//	        
////	        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
//			
//			ObjectMapper mapper = new ObjectMapper();	//JSON 변경용
//	    	Map<String, String> map = new HashMap<String, String>();
//	    	map.put("message", errorMessage);
////	    	map.put("code", "401");
//	    	
//			response.setStatus(HttpServletResponse.SC_OK);
//	    	response.setCharacterEncoding("UTF-8");
//	        response.setHeader("content-type", "application/json");
//	        response.getWriter().write(mapper.writeValueAsString(map));
//	        logger.info(mapper.writeValueAsString(map));
//	        response.getWriter().flush();
//	        response.getWriter().close();
//		} else {
//			logger.info("=================================================");
//			logger.info("리다이렉트 로그인 페이지 ");
//			logger.info("=================================================");
//			response.sendRedirect("/comm/loginPage");			
//		}
//		
		
//		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
