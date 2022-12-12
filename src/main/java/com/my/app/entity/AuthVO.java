package com.my.app.entity;

import lombok.Data;

@Data
public class AuthVO {
	private int tno; // 인덱스
	private int userNo; // user 참조키 
	private String auth; // 권한
}
