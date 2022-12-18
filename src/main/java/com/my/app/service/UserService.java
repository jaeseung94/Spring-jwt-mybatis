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
import org.springframework.stereotype.Service;

import com.my.app.dto.AuthDto;
import com.my.app.dto.UserDto;
import com.my.app.entity.UserVO;
import com.my.app.mapper.UserMapper;
import com.my.app.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);     
    
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
        
        String accessToken = jwtUtils.createAccessToken(id);
        String refreshToken = jwtUtils.createRefreshToken(id);
        
        logger.info("=================================================");
        logger.info("accessToken :  " + accessToken);
        logger.info("refreshToken :  " + refreshToken);
        
        logger.info("=================================================");
        logger.info("UserService 서비스 createToken() 및 refreshToken() ");		
        
        // refresh 토큰 저장
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("refreshToken", refreshToken);
        
        userMapper.updateRefreshToken(map);        
        UserVO user = userMapper.getUser(id);
        
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
    
    public UserDto getMyUserInfo() {
    	String username = SecurityUtil.getCurrentUsername();
    	if(username == null) {
    		new RuntimeException("로그인 유저 정보가 없습니다.");    		
    	} 
    	
    	UserVO user = userMapper.getUser(username);
    	return UserDto.builder()
    			.id(user.getId())
    			.email(user.getEmail())
    			.cellno(user.getCellno())
    			.name(user.getName())
    			.regDate(user.getRegDate())
    			.authList(user.getAuthList().stream().map(authority -> AuthDto.builder().auth(authority.getAuth()).build()).collect(Collectors.toList()))    
    			.build();      
    	
    }
    
    public int updateUserInfo(UserDto userDto) {
    	return userMapper.updateUser(userDto);
    }
    
    public UserDto findUserByRefreshToken(String token) {
    	return userMapper.findUserByRefreshToken(token);    	
    }
    
    public int removeRefreshToken(String username) {
    	return userMapper.removeRefreshToken(username);
    }
    
    public int updateRefreshToken(Map<String, String> map) {
    	return userMapper.updateRefreshToken(map);       	
    }
    
}
