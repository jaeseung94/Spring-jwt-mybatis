package com.my.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.my.app.entity.UserVO;
import com.my.app.mapper.UserMapper;
import com.my.app.security.entity.CustomUser;

import lombok.RequiredArgsConstructor;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
//	private final UserRepository userRepository;
	private final UserMapper mapper;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) {
		logger.info("=================================================");
		logger.info("loadUserByUsername 실행 ");
		logger.info("username : " + username);
		logger.info("=================================================");
		
		UserVO vo = mapper.getUser(username);
		logger.info("vo : " + vo);
		if(vo == null) {
			logger.info("사용자를 찾을 수 없습니다. ");
			throw new UsernameNotFoundException(username + " -> 사용자를 찾을 수 없습니다.");
		}
		
		return new CustomUser(vo);
		
//		return userRepository.findOneWithAuthoritiesByUsername(username).map(user -> createUser(username, user))
//				.orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
	}

//	private org.springframework.security.core.userdetails.User createUser(String username, User user) {
//		if (!user.isActivated()) {
//			throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
//		}
//
//		List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
//				.map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
//				.collect(Collectors.toList());
//
//		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
//				grantedAuthorities);
//	}
}

