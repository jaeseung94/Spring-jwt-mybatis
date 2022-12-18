package com.my.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.app.dto.UserDto;
import com.my.app.entity.UserVO;
import com.my.app.service.CommonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comm")
public class CommonRestController {
    
    private static final Logger logger = LoggerFactory.getLogger(CommonRestController.class); 
    private final CommonService commService; 

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/content")    
    public String userContent() {
        return "user Content";
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
    
}
