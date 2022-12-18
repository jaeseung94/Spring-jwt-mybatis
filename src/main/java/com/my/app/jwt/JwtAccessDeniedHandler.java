package com.my.app.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
	private static final Logger logger = LoggerFactory.getLogger(JwtAccessDeniedHandler.class);

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException {
		// 필요한 권한이 없이 접근하려 할때 403
		logger.info("========================================================");
		logger.info("JwtAccessDeniedHandler 실행");
		logger.info("========================================================");
		//response.sendError(HttpServletResponse.SC_FORBIDDEN);
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	    response.setStatus(HttpServletResponse.SC_FORBIDDEN);

	    final Map<String, Object> body = new HashMap<>();
	    body.put("status", HttpServletResponse.SC_FORBIDDEN);
	    body.put("error", "unauthenticated");
	    body.put("message", accessDeniedException.getMessage());
	    body.put("path", request.getServletPath());

	    final ObjectMapper mapper = new ObjectMapper();
	    mapper.writeValue(response.getOutputStream(), body);
	}
}
