package com.my.app.service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.app.controller.CommonRestController;
import com.my.app.dto.AuthDto;
import com.my.app.dto.UserDto;
import com.my.app.entity.UserVO;
import com.my.app.exception.DuplicateMemberException;
import com.my.app.jwt.TokenProvider;
import com.my.app.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);   

    public void updateRefreshToken(Map<String, String> map) {
    	mapper.updateRefreshToken(map);    	
    }
    
    public UserDto login(String id, String password) {
    	logger.info("=================================================");
		logger.info("UserService 서비스 authenticate");

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
        
        String accessToken = tokenProvider.createToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);
        
        logger.info("=================================================");
        logger.info("accessToken :  " + accessToken);
        logger.info("refreshToken :  " + refreshToken);
        
        logger.info("=================================================");
        logger.info("UserService 서비스 createToken() 및 refreshToken() ");		
        
        // refresh 토큰 저장
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("refreshToken", refreshToken);
        
        this.updateRefreshToken(map);        
        UserVO user = mapper.getUser(id);
        
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .cellno(user.getCellno())
                .name(user.getName())
                .regDate(user.getRegDate())
                .authList(user.getAuthList().stream().map(authority -> AuthDto.builder().auth(authority.getAuth()).build()).collect(Collectors.toList()))    
                .accessToken(accessToken)
        		.refreshToken(refreshToken)
                .build();       
        
    }
    
}
