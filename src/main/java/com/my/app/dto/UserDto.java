package com.my.app.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.my.app.entity.AuthVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	private String id; // 아이디
	private String email; // 이메일
	private String cellno; // 핸드폰번호
	private String name; // 성명
	private Date regDate; // 등록일
	private Date updateDate; // 수정일
	private List<AuthDto> authList; // 권한 정보
	private String accessToken;
	private String refreshToken;
	private String authProvider;
}