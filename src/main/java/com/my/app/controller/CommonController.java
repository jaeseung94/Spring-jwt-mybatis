package com.my.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comm")
public class CommonController {
    
    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);   

    @GetMapping("/loginPage")
    public String loginPage() {
        return "/common/loginPage";
    }
    
    @GetMapping("/index")
    public String index() {
        return "/user/index";
    }

	@GetMapping("/signUp")
	public String signup() {
		return "/common/signUp";
	}
	
	    
}
