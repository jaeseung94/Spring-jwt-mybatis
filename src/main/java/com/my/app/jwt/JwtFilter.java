package com.my.app.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;

//public class JwtFilter extends GenericFilterBean {
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter  {

   private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
//   public static final String ACCESSTOKEN_HEADER = "jwt-access-token";
//   public static final String REFRESH_HEADER = "jwt-refresh-token";
   public static final String AUTHORIZATION_HEADER = "Authorization";
   private final TokenProvider tokenProvider;
//   public JwtFilter(TokenProvider tokenProvider) {
//      this.tokenProvider = tokenProvider;
//   }

   @Override
//   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
		   throws IOException, ServletException {
      //HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
      //String jwt = resolveToken(httpServletRequest);
      String jwt = resolveToken(request);
      //String requestURI = httpServletRequest.getRequestURI();
      String requestURI = request.getRequestURI();
      
      logger.info("===============================================");
      logger.info("JwtFilter 실행 ");
      logger.info("tokenProvider :  " + tokenProvider);
      logger.info("===============================================");
      
      try {
    	  if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
    		  Authentication authentication = tokenProvider.getAuthentication(jwt);
    		  SecurityContextHolder.getContext().setAuthentication(authentication);
    		  logger.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
    	  } else {
    		  logger.info("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
    	  }
    	  
      } catch(Exception e) {
    	  logger.info("===============================================");
    	  logger.info("토큰 에러 발생");
    	  logger.info(e.getMessage());
    	  logger.info("===============================================");
      }

//      filterChain.doFilter(servletRequest, servletResponse);
      filterChain.doFilter(request, response);
   }

   private String resolveToken(HttpServletRequest request) {
      String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
         return bearerToken.substring(7);
      }

      return null;
   }
}
