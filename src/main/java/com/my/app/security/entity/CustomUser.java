package com.my.app.security.entity;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.my.app.entity.UserVO;

import lombok.Getter;

@Getter
public class CustomUser extends User implements OAuth2User{	
	
	private static final long serialVersionUID = 1L;
	
	//private UserVO user;
    private Map<String, Object> attributes;
	
	public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
	
	public CustomUser(UserVO vo) {
		super(vo.getId(), vo.getPassword(), vo.getAuthList().stream().map(auth -> new SimpleGrantedAuthority(auth.getAuth())).collect(Collectors.toList()));		
		//this.user = vo;
	}
	
	public CustomUser(UserVO vo, Map<String, Object> attributes) {
		super(vo.getId(), vo.getPassword(), vo.getAuthList().stream().map(auth -> new SimpleGrantedAuthority(auth.getAuth())).collect(Collectors.toList()));		
		//this.user = vo;
		this.attributes = attributes;
	}
	
	

	@Override
	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		return attributes;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return getUsername();
	}
}
