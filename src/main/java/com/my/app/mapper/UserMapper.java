package com.my.app.mapper;

import java.util.Map;

import com.my.app.dto.UserDto;
import com.my.app.entity.AuthVO;
import com.my.app.entity.UserVO;

public interface UserMapper {
	public UserVO getUser(String id); // 사용자조회
	public UserVO findUserByEmail(String email); // 사용자조회
	public int checkId(String id); // 아이디 중복 체크
	public int checkEmail(String email); // 이메일 중복 체크
	public int insertUser(UserVO vo); // 유저 등록
	public int insertUserByOAuth2(UserVO vo); // 유저 등록	
	public int insertUserAuth(AuthVO vo); // 권한 등록
	public int updateRefreshToken(Map<String, String> map); // refreshToken 저장
	public UserDto findUserByRefreshToken(String token);
	public int removeRefreshToken(String token);
	public int updateUser(UserDto userDto);
		
}
