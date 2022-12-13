package com.my.app.entity;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
@Data
public class UserVO {	
	private int tno; // 테이블번호
	private String id; // 아이디
	private String email; // 이메일
	private String cellno; // 핸드폰번호
	private String password; // 비밀번호
	private String name; // 성명
	private Date regDate; // 등록일
	private Date updateDate; // 수정일
	private List<AuthVO> authList; // 권한 정보
	private String refreshToken; // refreshToken
	
}
