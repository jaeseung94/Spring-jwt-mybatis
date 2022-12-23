package com.my.app.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.my.app.entity.AuthVO;
import com.my.app.entity.UserVO;
import com.my.app.exception.OAuthProcessingException;
import com.my.app.mapper.UserMapper;
import com.my.app.security.entity.AuthProvider;
import com.my.app.security.entity.CustomUser;
import com.my.app.security.entity.OAuth2UserInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);
	private final UserMapper userMapper;
	private final JwtUtils jwtUtils;
		
    // OAuth2UserRequest에 있는 Access Token으로 유저정보 get
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        return process(oAuth2UserRequest, oAuth2User);
    }

    // 획득한 유저정보를 Java Model과 맵핑하고 프로세스 진행
    @Transactional
    private OAuth2User process(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        AuthProvider authProvider = AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProvider, oAuth2User.getAttributes());
        String provider = authProvider.name(); // GOOGLE
        
        logger.info("============================================");
        logger.info("CustomOAuth2UserService 실행 ");
        logger.info("============================================");

        if (userInfo.getEmail().isEmpty()) {
            throw new OAuthProcessingException("Email not found from OAuth2 provider");
        }        
        
        UserVO findUser = userMapper.findUserByEmail(userInfo.getEmail());

        if (findUser != null) { // 이미 가입된 경우
        	logger.info("==================================================");
        	logger.info(provider);
        	logger.info(findUser.getAuthProvider());
        	logger.info("==================================================");
            if (!provider.equals(findUser.getAuthProvider())) {
                throw new OAuthProcessingException("Wrong Match Auth Provider");
            }

        } else { // 가입되지 않은 경우 회원가입 처리        	
        	// 임의 패스워드 생성
			String uuid = UUID.randomUUID().toString().substring(0, 6);
			String password = new BCryptPasswordEncoder().encode("패스워드" + uuid);
    		
			AuthVO authVo = new AuthVO();
			authVo.setAuth("ROLE_USER");        
			
			findUser = UserVO.builder()
    		    .id(userInfo.getId())
    		    .email(userInfo.getEmail())
    		    .password(password)
    		    .name(userInfo.getName())
    		    .authList(Collections.singletonList(authVo))
    		    .authProvider(provider)
    		    .build();  
    		
    		
    		userMapper.insertUserByOAuth2(findUser);
    		authVo.setUserNo(findUser.getTno());
    		userMapper.insertUserAuth(authVo);	    		
        }
        
       
                
        return new CustomUser(findUser, oAuth2User.getAttributes());
    }
}
