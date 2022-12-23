//package com.my.app.security.entity;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import com.my.app.entity.UserVO;
//
//import lombok.Getter;
//import lombok.ToString;
//
//@Getter 
//@ToString
//public class PrincipalDetails implements UserDetails {
//
//    private UserVO user;
//
//    public PrincipalDetails(UserVO user) {
//        this.user = user;
//    }
//
//    /**
//     * UserDetails 구현
//     * 해당 유저의 권한목록 리턴
//     */
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//
//        Collection<GrantedAuthority> collect = new ArrayList<>();
//        collect.add(new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                return user.getAuthList().toString();
//            }
//        });
//        return collect;
//    }
//
//
//    /**
//     * UserDetails 구현
//     * 비밀번호를 리턴
//     */
//    @Override
//    public String getPassword() {
//        return user.getPassword();
//    }
//
//
//    /**
//     * UserDetails 구현
//     * PK값을 반환해준다
//     */
//    @Override
//    public String getUsername() {
//        return user.getId();
//    }
//
//
//    /**
//     * UserDetails 구현
//     * 계정 만료 여부
//     *  true : 만료안됨
//     *  false : 만료됨
//     */
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//
//    /**
//     * UserDetails 구현
//     * 계정 잠김 여부
//     *  true : 잠기지 않음
//     *  false : 잠김
//     */
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//
//    /**
//     * UserDetails 구현
//     * 계정 비밀번호 만료 여부
//     *  true : 만료 안됨
//     *  false : 만료됨
//     */
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//
//    /**
//     * UserDetails 구현
//     * 계정 활성화 여부
//     *  true : 활성화됨
//     *  false : 활성화 안됨
//     */
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//}