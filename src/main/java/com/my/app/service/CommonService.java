package com.my.app.service;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.app.dto.AuthDto;
import com.my.app.dto.UserDto;
import com.my.app.entity.AuthVO;
import com.my.app.entity.UserVO;
import com.my.app.exception.DuplicateMemberException;
import com.my.app.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommonService {
    private final UserMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(CommonService.class);   

    @Transactional
    public UserDto signUp(UserVO vo) {
        if (mapper.checkId(vo.getId()) > 0) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }
        
        if (mapper.checkEmail(vo.getEmail()) > 0) {
            throw new DuplicateMemberException("이미 가입되어 있는 이메일입니다.");
        }
        
        AuthVO authVo = new AuthVO();
		authVo.setAuth("ROLE_USER");        

        logger.info("========================================");
		logger.info("CommonService signUp");
		logger.info("vo : " + vo);
		logger.info("========================================");
		
		// 회원 등록
		vo.setPassword(new BCryptPasswordEncoder().encode(vo.getPassword()));
		//vo.setCellno(vo.getCellno().replaceAll("-", ""));
		mapper.insertUser(vo);
		authVo.setUserNo(vo.getTno());
		mapper.insertUserAuth(authVo);		
		
		UserVO user = mapper.getUser(vo.getId());
		
		UserDto dto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .cellno(user.getCellno())
                .name(user.getName())
                .regDate(user.getRegDate())
                .authList(user.getAuthList().stream().map(authority -> AuthDto.builder().auth(authority.getAuth()).build()).collect(Collectors.toList()))                        
                .build();
		
        return dto;
    }    
      
    public int checkId(String username) {    	    	
    	return mapper.checkId(username);
    }
    
    public int checkEmail(String email) {    	    	
    	return mapper.checkEmail(email);
    }

}
