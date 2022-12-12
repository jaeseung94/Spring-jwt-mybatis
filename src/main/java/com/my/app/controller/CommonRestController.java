package com.my.app.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.my.app.dto.TokenDto;
import com.my.app.dto.UserDto;
import com.my.app.entity.UserVO;
import com.my.app.jwt.JwtFilter;
import com.my.app.jwt.TokenProvider;
import com.my.app.service.CommonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comm")
public class CommonRestController {
    private final CommonService commService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    
    private static final Logger logger = LoggerFactory.getLogger(CommonRestController.class);   

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }  
	
	@PostMapping("/signUp")
	public ResponseEntity<UserDto> signUp(@RequestBody UserVO user) {
		logger.info("========================================");
		logger.info("회원 등록");		
		logger.info("========================================");
		return new ResponseEntity<>(commService.signUp(user), HttpStatus.OK);
	}
	
	// 아이디 중복 검사
	@GetMapping("/checkId")
	public ResponseEntity<Integer> checkId(String id) {
		return new ResponseEntity<>(commService.checkId(id), HttpStatus.OK);
	}
	
	// 이메일 중복 검사
	@GetMapping("/checkEmail")
	public ResponseEntity<Integer> checkEmail(String email) {
		return new ResponseEntity<>(commService.checkEmail(email), HttpStatus.OK);
	}	
	
	// 로그인
	@PostMapping("/login")
    public ResponseEntity<TokenDto> authorize(@RequestBody HashMap<String, Object> param) {
    	
    	logger.info("=================================================");
		logger.info("AuthController 컨트롤러 authenticate");

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(param.get("id"), param.get("password"));
        
        logger.info("=================================================");
		logger.info("AuthController 컨트롤러 UsernamePasswordAuthenticationToken 토큰");		
		
		// loadUserByname 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        logger.info("=================================================");
        logger.info("AuthController 컨트롤러 authentication 객체");	
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        logger.info("=================================================");
        logger.info("AuthController 컨트롤러 setAuthentication() ");	
        
        String jwt = tokenProvider.createToken(authentication);
        
        logger.info("=================================================");
		logger.info("AuthController 컨트롤러 createToken()");		

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    
}
