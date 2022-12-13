package com.my.app.TokenProviderTests;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
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
import com.my.app.jwt.TokenProvider;
import com.my.app.mapper.UserMapper;
import com.my.app.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@RequiredArgsConstructor
public class TokenProviderTests {
	
	private static final Logger logger = LoggerFactory.getLogger(TokenProviderTests.class);
	
	private UserMapper mapper;
	private UserService userService;	
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final TokenProvider tokenProvider;

	@Test
	public void testSignUp() {
		String id = "test";
		String password = "test";
		
		UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(id, password);
        
        logger.info("=================================================");
		logger.info("UserService 서비스 UsernamePasswordAuthenticationToken 토큰");		
		
		// loadUserByname 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        logger.info("=================================================");
        logger.info("UserService 서비스 authentication 객체");	
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        logger.info("=================================================");
        logger.info("UserService 서비스 setAuthentication() ");	
        
//        String accessToken = tokenProvider.createToken(authentication);
//        String refreshToken = tokenProvider.createRefreshToken(authentication);
//        userService.updateRefreshToken(id, refreshToken);        
//        UserVO user = mapper.getUser(id);
//        
//        UserDto.builder()
//                .id(user.getId())
//                .email(user.getEmail())
//                .cellno(user.getCellno())
//                .name(user.getName())
//                .regDate(user.getRegDate())
//                .authList(user.getAuthList().stream().map(authority -> AuthDto.builder().auth(authority.getAuth()).build()).collect(Collectors.toList()))    
//                .accessToken(accessToken)
//        		.refreshToken(refreshToken)
//                .build();       
//			
	}
	
}
