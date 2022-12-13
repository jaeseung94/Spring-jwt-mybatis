package com.my.app.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.app.entity.UserVO;
import com.my.app.jwt.TokenProvider;
import com.my.app.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/hello")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<String> hello() {
    	logger.info("=================================");
    	logger.info("hello ");
    	logger.info("=================================");
    	
        return ResponseEntity.ok("hello");
    }  
    
    @GetMapping("/index")
    public String index() {
        return "/user/index";
    }
    
    @GetMapping("/myPage")
    public String myPage() {
    	logger.info("=================================");
    	logger.info("myPage ");
    	logger.info("=================================");
    	return "/user/myPage"; 
    }
    
    @PostMapping("/myPage")
    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    public ResponseEntity<String> myPage(String id, String accessToken) {
    	logger.info("=================================");
    	logger.info("myPage ");
    	logger.info("=================================");
    	return ResponseEntity.ok("myPage 데이터!!");
    }

    @PostMapping("/test-redirect")
    public void testRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/user");
    } 
    
    @PostMapping("/refreshToken")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody UserVO user, HttpServletResponse response){
    	
    	Map<String, String> map = new HashMap<String, String>();
    	if(tokenProvider.validateToken(user.getRefreshToken())) {
    		//map.put(key, value)
    	}
    	
    	return new ResponseEntity<Map<String,String>>(map, HttpStatus.ACCEPTED);
    }

//    @GetMapping("/user")
//    @PreAuthorize("hasAnyRole('USER','ADMIN')")
//    public ResponseEntity<UserDto> getMyUserInfo(HttpServletRequest request) {
//        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
//    }
//
//    @GetMapping("/user/{username}")
//    @PreAuthorize("hasAnyRole('ADMIN')")
//    public ResponseEntity<UserDto> getUserInfo(@PathVariable String username) {
//        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
//    }
    
//    @PostMapping("/authenticate")
//    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {
//    	
//    	logger.info("=================================================");
//		logger.info("AuthController 컨트롤러 authenticate");
//
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
//        
//        logger.info("=================================================");
//		logger.info("AuthController 컨트롤러 UsernamePasswordAuthenticationToken 토큰");		
//		
//		// loadUserByname 실행
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        logger.info("=================================================");
//        logger.info("AuthController 컨트롤러 authentication 객체");	
//        
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        
//        logger.info("=================================================");
//        logger.info("AuthController 컨트롤러 setAuthentication() ");	
//        
//        String jwt = tokenProvider.createToken(authentication);
//        
//        logger.info("=================================================");
//		logger.info("AuthController 컨트롤러 createToken()");		
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
//
//        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
//    }
    
    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok("관리자만 접근");
        
    }
    
}
