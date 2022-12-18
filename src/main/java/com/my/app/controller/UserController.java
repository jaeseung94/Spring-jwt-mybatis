package com.my.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {	

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

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

}
