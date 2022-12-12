package com.my.app.MapperTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.my.app.entity.AuthVO;
import com.my.app.entity.UserVO;
import com.my.app.mapper.UserMapper;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class UserMapperTests {
	
	@Autowired
	private UserMapper mapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
//	@Test
//	public void testCheckEmail() {
//		int result = mapper.checkEmail("test@test.mail");
//		log.info("========================================");
//		log.info("result : " + result);		
//		log.info("========================================");
//			
//	}
	
//	@Test
//	public void testCheckId() {
//		int result = mapper.checkId("tester");
//		log.info("========================================");
//		log.info("result : " + result);		
//		log.info("========================================");
//			
//	}
//	
	@Test
	@Transactional // 롤백
	public void testSignUp() {
		
		UserVO vo = new UserVO();
		vo.setId("tester3");
		vo.setPassword(passwordEncoder.encode("test"));
		vo.setEmail("test3@mail");
		vo.setName("tester3");
		vo.setCellno("324234234");
		
		AuthVO authVo = new AuthVO();
		authVo.setAuth("member");
		
		mapper.insertUser(vo);
		authVo.setUserNo(vo.getTno());
		log.info("===============vo========================");
		log.info("uservo : " + vo);	
		log.info("authVo : " + authVo);	
		mapper.insertUserAuth(authVo);
//		log.info("result : " + result);		
		log.info("========================================");
			
	}
	
}
