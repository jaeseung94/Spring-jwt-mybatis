package com.my.app.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.app.entity.UserVO;
import com.my.app.exception.DuplicateMemberException;
import com.my.app.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

//    @Transactional
//    public void signUp(UserVO vo) {
//        if (mapper.checkId(vo.getId()) > 0) {
//            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
//        }
//        
//        if (mapper.checkEmail(vo.getEmail()) > 0) {
//            throw new DuplicateMemberException("이미 가입되어 있는 이메일입니다.");
//        }
//
//        UserVO user = UserVO.builder()
//                .id(vo.getId())
//                .password(passwordEncoder.encode(vo.getPassword()))
//                .email(vo.getEmail())
//                .cellno(vo.getEmail())
//                .name(vo.getName())
//                .build();
//
//         mapper.signUp(user);
//    }

}
