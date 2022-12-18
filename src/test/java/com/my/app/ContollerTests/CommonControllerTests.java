package com.my.app.ContollerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.my.app.controller.CommonController;
import com.my.app.controller.UserController;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class CommonControllerTests {
	
	@Autowired
	private CommonController controller ;
	private UserController userController ;
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}	


	
//	@Test
//	public void testModify() throws Exception {
//		String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/board/modify")
//				.param("bno", "1")
//				.param("title", "수정된 테스트 새글 제목1")
//				.param("content", "수정된테스트 새글 내용1")
//				.param("writer", "user01")
//				).andReturn().getModelAndView().getViewName();                                                                                                                                    
//		
//		log.info(resultPage);
//				
//	}
//	
//	
//	@Test
//	public void testGet() throws Exception {
//		log.info("-----------------------------------------------2");
//		 mockMvc.perform(MockMvcRequestBuilders.get("/board/get")
//				.param("bno", "1")
//				).andReturn().getModelAndView().getModelMap();
//		 log.info("-----------------------------------------------2");		
//	}
//	

}

