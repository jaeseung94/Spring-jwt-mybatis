package com.my.app.service;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.my.app.dto.UserDto;
import com.my.app.exception.TokenRefreshException;
import com.my.app.mapper.UserMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtils implements InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	private static final String AUTHORITIES_KEY = "auth";
	@Value("${jwt.secret}")
	private String secret;
	@Value("${jwt.accessToken-validity-in-seconds}")
	private long accessTokenValidityInseconds;
	@Value("${jwt.refreshToken-validity-in-seconds}")
	private long refreshTokenValidityInseconds;
	private final UserMapper userMapper;
	private final UserDetailsService userDetailsService;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private Key key;

//	public JwtUtils(@Value("${jwt.secret}") String secret,
//			@Value("${jwt.accessToken-validity-in-seconds}") long tokenValidityInSeconds,
//			@Value("${jwt.refreshToken-validity-in-seconds}") long refreshTokenValidityInSeconds
//			) {
//		this.secret = secret;
//		this.accessTokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
//		this.refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 1000;
//	}

	// TokenProvider 빈 생성이 되고 생성자 의존성 주입 받은 후 키값 저장
	@Override
	public void afterPropertiesSet() {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	// access 토큰 생성
	public String createAccessToken(String username) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
				userDetails.getAuthorities());

		String authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		return Jwts.builder().setSubject(authentication.getName()).claim(AUTHORITIES_KEY, authorities)
				.signWith(key, SignatureAlgorithm.HS512)
				.setExpiration(new Date((new Date()).getTime() + refreshTokenValidityInseconds * 1000)).compact();

//		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
//				.setExpiration(new Date((new Date()).getTime() + refreshTokenValidityInseconds * 1000))
//				.signWith(key, SignatureAlgorithm.HS256).compact();
	}
	
	// access토큰 생성
	public String createAccessToken(Authentication authentication) {
		String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		long now = (new Date()).getTime();
		Date validity = new Date(now + this.refreshTokenValidityInseconds * 1000);

		return Jwts.builder().setSubject(authentication.getName()).claim(AUTHORITIES_KEY, authorities)
				.signWith(key, SignatureAlgorithm.HS512).setExpiration(validity).compact();
	}
	
	
	// refresh 토큰 생성
	public String createRefreshToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + refreshTokenValidityInseconds * 1000))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}
	
	// refreshToken 조회
	public UserDto findUserByRefreshToken(String refreshToken) {
		UserDto userDto = userMapper.findUserByRefreshToken(refreshToken);
		if(userDto == null) {
			throw new TokenRefreshException(refreshToken,
		            "Refresh token is not in database!");
		} else {
			return userDto;
		}
	}
	
	// 토큰 유효기간 검사
	public String verifyRefreshExpiration(String token) {
		
		try {
			Date expireDate = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody()
					.getExpiration();			

		} catch (Exception e) {
			throw new TokenRefreshException(token,
					"refresh 토큰이 만료되었습니다. 로그인이 필요합니다.");
		}
		return token;
	}
	
	// accessToken 재발급
	// RTR(Refresh Token Rotation)으로 엑세스토큰 재발급시 리프레쉬토큰도 재발급
	public Map<String, String> reIssueAccessToken(String refreshToken) {
		
//		String refreshToken = jwtUtils.resolveToken()
		Map<String, String> map = new HashMap<String, String>();
		String resultToken = this.verifyRefreshExpiration(refreshToken);		
		UserDto userDto = this.findUserByRefreshToken(resultToken);		
		String accessToken = this.createAccessToken(userDto.getId());		
		String newRefreshToken = this.createRefreshToken(userDto.getId());
		
		map.put("accessToken", accessToken);
		map.put("refreshToken", newRefreshToken);	
		map.put("id", userDto.getId());
		
		return map;	

	}

	// 토큰으로 사용자 아이디 조회
	public String getUserNameFromJwtToken(String token) {
		logger.info(token);
		System.out.println(key);
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

	// 토큰 유효성 검사
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			logger.info("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			logger.info("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			logger.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			logger.info("JWT 토큰이 잘못되었습니다.");
		}
		return false;
	}

	// 헤더에서 토큰 조회
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		
		return null;
//		String token = null;
//		
//		Cookie cookie = WebUtils.getCookie(request, tokenType);
//		if (cookie != null) {
//			token = cookie.getValue();
//		}
//		return token;
	}
	
	// 토큰을 이용하여 인증 조회
	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		
		Collection<? extends GrantedAuthority> authorities = Arrays
				.stream(claims.get("auth").toString().split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		
		User principal = new User(claims.getSubject(), "", authorities);
		
		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}


	
//	// 토큰 생성
//	public String createToken(Authentication authentication) {
//		String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
//				.collect(Collectors.joining(","));
//		
//		logger.info("===============================================");
//		logger.info("authorities : " + authorities);
//		logger.info("===============================================");
//
//		long now = (new Date()).getTime();
//		Date validity = new Date(now + this.tokenValidityInMilliseconds);
//
//		return Jwts.builder().setSubject(authentication.getName()).claim(AUTHORITIES_KEY, authorities)
//				.signWith(key, SignatureAlgorithm.HS256).setExpiration(validity).compact();
//	}
}

