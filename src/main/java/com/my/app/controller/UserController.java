package com.my.app.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.app.dto.LoginDto;
import com.my.app.dto.TokenDto;
import com.my.app.dto.UserDto;
import com.my.app.jwt.JwtFilter;
import com.my.app.jwt.TokenProvider;
import com.my.app.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @GetMapping("/index")
    public String index() {
        return "/user/index";
    }

    @PostMapping("/test-redirect")
    public void testRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/user");
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
    
    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {
    	
    	logger.info("=================================================");
		logger.info("AuthController 컨트롤러 authenticate");

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        
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
    
//    @GetMapping("/loginPage")
//    public String login(@RequestParam(value = "error", required = false)String error,
//                        @RequestParam(value = "exception", required = false)String exception,
//                        Model model) {
//    	logger.info("================================");
//    	logger.info("getmapping loginPage");
//    	logger.info("error : " + error);
//    	logger.info("exception : " + exception);
//    	logger.info("================================");
//        model.addAttribute("error", error);
//        model.addAttribute("exception", exception);
//        model.addAttribute("asd", "asd");
////        return "/user/index";
//        return "loginPage";
//    }
    
}
