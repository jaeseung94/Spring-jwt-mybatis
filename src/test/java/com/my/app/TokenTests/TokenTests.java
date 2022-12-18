package com.my.app.TokenTests;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.my.app.dto.AuthDto;
import com.my.app.dto.UserDto;
import com.my.app.entity.UserVO;
import com.my.app.mapper.UserMapper;
import com.my.app.service.JwtUtils;
import com.my.app.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenTests {
	
	private static final Logger logger = LoggerFactory.getLogger(TokenTests.class);
	
	//private UserMapper mapper;
	//private UserService userService;	
	//private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtUtils jwtUtils;

//	@Test
//	public void testSignUp() {
//		String id = "test";
//		String password = "test";
//		
//		UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(id, password);
//        
//        logger.info("=================================================");
//		logger.info("UserService 서비스 UsernamePasswordAuthenticationToken 토큰");		
//		
//		// loadUserByname 실행
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        logger.info("=================================================");
//        logger.info("UserService 서비스 authentication 객체");	
//        
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        
//        logger.info("=================================================");
//        logger.info("UserService 서비스 setAuthentication() ");		
//	}
	
	@Test
	public void testGetUsernameBytoken() {
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0IiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY3MTI4MDYxNn0.PxCahdWFNti7oCmBn-HSMPsrxxQacNCpeHDwPfDEowEcW1nQwouWqFo-5DvozTSrC4a-No9byYuyPJGc5oqRlA";
		String username = jwtUtils.getUserNameFromJwtToken(token);
		logger.info("======================================");
		logger.info(username);
		logger.info("======================================");
	}
	
}
