package com.my.app.service;

import java.util.Map;

import com.my.app.security.entity.AuthProvider;
import com.my.app.security.entity.GoogleOAuth2UserInfo;
import com.my.app.security.entity.OAuth2UserInfo;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(AuthProvider authProvider, Map<String, Object> attributes) {
        switch (authProvider) {
            case GOOGLE: return new GoogleOAuth2UserInfo(attributes);
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}
