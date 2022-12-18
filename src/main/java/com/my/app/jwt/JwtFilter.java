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

import com.my.app.service.JwtUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
	private final JwtUtils jwtUtils;
	//private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		logger.info("===============================================");
		logger.info("JwtFilter 실행 ");
		logger.info("===============================================");

		try {
			String accessToken = jwtUtils.resolveToken(request);
			String requestURI = request.getRequestURI();
			
			if (StringUtils.hasText(accessToken) && jwtUtils.validateToken(accessToken)) {
//				String username = jwtUtils.getUserNameFromJwtToken(accessToken);
//
//				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//						userDetails, null, userDetails.getAuthorities());
//				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//				SecurityContextHolder.getContext().setAuthentication(authentication);
				 Authentication authentication = jwtUtils.getAuthentication(accessToken);
		         SecurityContextHolder.getContext().setAuthentication(authentication);
				
				logger.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
			} else {	
				logger.info("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
			}

		} catch (Exception e) {
			logger.info("===============================================");
			logger.info("토큰 에러 발생");
			logger.info(e.getMessage());
			logger.info("===============================================");
		}

		filterChain.doFilter(request, response);
	}

}
