package com.my.app.OAuth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.my.app.mapper.UserMapper;
import com.my.app.service.JwtUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtUtils jwtUtils;
	private final UserMapper userMapper;

	private static final Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		logger.info("============================================");
		logger.info("OAuth2AuthenticationSuccessHandler 실행 ");
		logger.info("============================================");

		String id = authentication.getName();
		String accessToken = jwtUtils.createAccessToken(id);
		String refreshToken = jwtUtils.createRefreshToken(id);

		// refresh 토큰 저장
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("refreshToken", refreshToken);

		userMapper.updateRefreshToken(map);

		Cookie cookie = new Cookie("refresh-token", refreshToken);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		
		getRedirectStrategy().sendRedirect(request, response, "/user/myPage");
	}

}