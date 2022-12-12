package com.my.app.ContollerTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.my.app.controller.CommonRestController;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class CommonRestControllerTests {
	
	@Autowired
	private CommonRestController restController ;
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(restController).build();
	}
	
	@Test
	@Transactional // 테스트 완료 후 rollback
	public void testSignUp() throws Exception {
		mockMvc.perform(post("/comm/signUp")
		   .content("{\"id\":\"test2\", \"password\":\"test\", \"email\":\"test2@gmail.com\", \"name\":\"테스트2\", \"cellno\":\"01023456789\"}")
		   .contentType(MediaType.APPLICATION_JSON)
		   .accept(MediaType.APPLICATION_JSON))
		   .andExpect(status().isOk())
		   .andDo(print());				
	}
	
//	@Test
//	public void testLogin() throws Exception {
//		mockMvc.perform(post("/comm/login")
//		   .content("{\"id\":\"test\", \"password\":\"test\"}")
//		   .contentType(MediaType.APPLICATION_JSON)
//		   .accept(MediaType.APPLICATION_JSON))
//		   .andExpect(status().isOk())
//		   .andDo(print());	
//	}
	
//	@Test
//	public void testCheckId() throws Exception {
//		log.info(
//			mockMvc.perform(get("/comm/checkUsername")
//			.param("username", "tester"))
//			.andExpect(status().isOk()).andDo(print()));		
//	}
	
//	@Test
//	public void testCheckEmail() throws Exception {
//		log.info(
//			mockMvc.perform(get("/comm/checkEmail")
//			.param("email", "test@test.mail"))
//			.andExpect(status().isOk()).andDo(print()));		
//	}
//	
//	@Test
//	@Transactional // 테스트 완료 후 rollback
//	public void testSignUp() throws Exception {
//		mockMvc.perform(post("/comm/signUp")
//		   .content("{\"username\":\"test2\", \"password\":\"test\", \"email\":\"test2@gmail.com\", \"name\":\"테스트2\", \"cellno\":\"01023456789\"}")
//		   .contentType(MediaType.APPLICATION_JSON)
//		   .accept(MediaType.APPLICATION_JSON))
//		   .andExpect(status().isOk())
//		   .andDo(print());
		
//		String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/board/register")
//				.param("title", "테스트 새글 제목")
//				.param("content", "테스트 새글 내용")
//				.param("writer", "user00")
//				).andReturn().getModelAndView().getViewName();                                                                                                                                    
		
//		log.info(resultPage);
				
//	}
	
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

