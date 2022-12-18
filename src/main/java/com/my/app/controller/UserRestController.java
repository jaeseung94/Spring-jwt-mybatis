package com.my.app.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import com.my.app.dto.TokenDto;
import com.my.app.dto.UserDto;
import com.my.app.entity.UserVO;
import com.my.app.exception.TokenRefreshException;
import com.my.app.service.JwtUtils;
import com.my.app.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserRestController {
    private final UserService userService;
    private final JwtUtils tokenProvider;
    private final JwtUtils jwtUtils;
    //private final AuthenticationManagerBuilder authenticationManagerBuilder;
    
    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @GetMapping("/hello")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> hello() {
    	logger.info("=================================");
    	logger.info("hello ");
    	logger.info("=================================");
    	
        return ResponseEntity.ok("hello");
    }  
    
	// 로그인
	@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> param, HttpServletRequest request, HttpServletResponse response) {	
    	
		UserDto userDto = userService.login(param.get("id"), param.get("password"));
		String accessToken = userDto.getAccessToken();
		String refreshToken = userDto.getRefreshToken();
		
		Cookie cookie = new Cookie("refresh-token", refreshToken);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		
        return ResponseEntity.ok(new TokenDto(userDto.getId(), accessToken, refreshToken));
    }
	
	// accessToken 재발급
	@PostMapping("/reIssueAccessToken")
	public ResponseEntity<?> reIssueAccessToken(HttpServletRequest request, HttpServletResponse response) {
		logger.info("=========================================");
		logger.info("reIssueAccessToken 컨트롤러 실행");
		logger.info("=========================================");	
		
		// 토큰 삭제 후 재생성
		Cookie refreshCookie = WebUtils.getCookie(request, "refresh-token");
		if(refreshCookie != null) {
			refreshCookie.setMaxAge(0);
			refreshCookie.setPath("/");
			refreshCookie.setHttpOnly(true);
			response.addCookie(refreshCookie);
		} else {
			new TokenRefreshException("","쿠키에 refresh토큰이 존재하지 않습니다.");
		}
		
		Map<String, String> map = jwtUtils.reIssueAccessToken(refreshCookie.getValue());
		
		userService.updateRefreshToken(map);
		
		
		Cookie cookie = new Cookie("refresh-token", map.get("refreshToken"));
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		
		return ResponseEntity.ok(map);
	}
    
	// test
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/content")    
    public String userContent() {
        return "user Content";
    }      
    
    // 사용자정보 조회
    @GetMapping("/getMyUserInfo")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDto> getMyUserInfo() {
    	logger.info("=================================");
    	logger.info("myPage ");
    	logger.info("=================================");    	
    	
    	return ResponseEntity.ok(userService.getMyUserInfo());
    }
    
    // 사용자정보 변경
    @PostMapping("/updateUserInfo")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDto> updateUserInfo(@RequestBody UserDto userDto) {
    	logger.info("=================================");
    	logger.info("updateUserInfo ");
    	logger.info(userDto.getName());
    	logger.info(userDto.getEmail());
    	logger.info(userDto.getCellno());
    	logger.info("=================================");    	
    	userService.updateUserInfo(userDto);
    	
    	return ResponseEntity.ok(userService.getMyUserInfo());
    }
    
    @PostMapping("/logout")
    @PreAuthorize("hasRole('USER')")
    public String logout(HttpServletRequest request, HttpServletResponse response){
    	logger.info("=================================");
    	logger.info("logout ");
    	logger.info("=================================");
    	String accessToken = jwtUtils.resolveToken(request);
    	String username = jwtUtils.getUserNameFromJwtToken(accessToken);
    	if(userService.removeRefreshToken(username) == 1) {
    		Cookie refreshCookie = WebUtils.getCookie(request, "refresh-token");
    		if(refreshCookie != null) {
    			refreshCookie.setMaxAge(0);		
    			refreshCookie.setPath("/");
    			refreshCookie.setHttpOnly(true);
    			response.addCookie(refreshCookie);
    			logger.info("====================");
    			logger.info("쿠키 삭제!");
    			logger.info("====================");
    		}
    	}
    	return "로그아웃처리";
    }

//  
    
//    @PostMapping("/refreshToken")
//    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody UserVO user){
//    	
//    	Map<String, String> map = new HashMap<String, String>();
//    	if(tokenProvider.validateToken(user.getRefreshToken())) {
//    		//map.put(key, value)
//    	}
//    	
//    	return new ResponseEntity<Map<String,String>>(map, HttpStatus.ACCEPTED);
//    }
    
    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok("관리자만 접근");
        
    }
    
}
