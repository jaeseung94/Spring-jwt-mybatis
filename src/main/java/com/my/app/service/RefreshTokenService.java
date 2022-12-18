//package com.my.app.service;
//
//import java.security.Key;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.util.WebUtils;
//
//import com.my.app.dto.UserDto;
//import com.my.app.exception.TokenRefreshException;
//import com.my.app.jwt.accessTokenService;
//import com.my.app.mapper.RefreshTokenMapper;
//import com.my.app.mapper.UserMapper;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class RefreshTokenService implements InitializingBean {
//	private final UserMapper mapper;
//	private final RefreshTokenMapper refreshMapper;
//	private final JwtUtils jwtUtils;
//	private final CustomUserDetailsService customUserDetailsService;
//	private final AuthenticationManagerBuilder authenticationManagerBuilder;
//	private Key key;
//
//	@Value("${jwt.refreshToken-validity-in-seconds}")
//	long refreshTokenValidityInSeconds;
//
//	@Value("${jwt.secret}")
//	String secret;
//
//	private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);
//
//	// accessTokenService 빈 생성이 되고 생성자 의존성 주입 받은 후 키값 저장
//	@Override
//	public void afterPropertiesSet() {
//		this.key = Keys.hmacShaKeyFor(secret.getBytes());
//	}	
//
//	// reftreshtoken 발급
//	public String createRefreshToken(String username) {
//		long now = (new Date()).getTime();
//		Date validity = new Date(now + this.refreshTokenValidityInSeconds * 1000);
//		
//		String token = Jwts.builder().setSubject(username).signWith(key, SignatureAlgorithm.HS256)
//				.setExpiration(validity).compact();
//		
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("token", token);
//		map.put("id", username);
//		refreshMapper.saveRefreshToken(map);
//
//		return token;
//	}
//	
//	// refreshToken 조회
//	public UserDto findUserByRefreshToken(String refreshToken) {
//		UserDto userDto = refreshMapper.findUserByRefreshToken(refreshToken);
//		if(userDto == null) {
//			throw new TokenRefreshException(refreshToken,
//		            "Refresh token is not in database!");
//		} else {
//			return userDto;
//		}
//	}
//	
//	// 토큰 유효기간 검사
//	public String verifyRefreshExpiration(String token) {
//		
//		try {
//			Date expireDate = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody()
//					.getExpiration();			
//			logger.error("======================================");
//			logger.error("expireDate : " + expireDate);	
//
//		} catch (Exception e) {
//			throw new TokenRefreshException(token,
//					"refresh 토큰이 만료되었습니다. 로그인이 필요합니다.");
//		}
//		return token;
//	}
//	
//	// accessToken 재발급
//	// RTR(Refresh Token Rotation)으로 엑세스토큰 재발급시 리프레쉬토큰도 재발급
//	public Map<String, String> reIssueAccessToken() {
//		
////		String refreshToken = jwtUtils.resolveToken()
//		Map<String, String> map = new HashMap<String, String>();
//		String resultToken = this.verifyRefreshExpiration(refreshToken);		
//		UserDto userDto = this.findUserByRefreshToken(resultToken);		
//		String accessToken = jwtUtils.createToken(userDto.getId());		
//		String newRefreshToken = this.createRefreshToken(userDto.getId());
//		
//		map.put("accessToken", accessToken);
//		map.put("refreshToken", newRefreshToken);
//		
////		Cookie cookie = new Cookie(key, value);
////		cookie.setPath(path);
////		cookie.setMaxAge(maxAge);
////		response.addCookie(cookie);
//		
//		return map;	
//
//	}
//	
//	// 토큰 조회
//	public String resolveToken(HttpServletRequest request) {
//
//		String token = null;
//		
//		Cookie cookie = WebUtils.getCookie(request, "refresh-token");
//		if (cookie != null) {
//			token = cookie.getValue();
//		}
//		return token;
//	}
//
//}
