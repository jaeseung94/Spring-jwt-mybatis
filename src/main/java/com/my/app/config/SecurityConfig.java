package com.my.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.my.app.jwt.JwtAccessDeniedHandler;
import com.my.app.jwt.JwtAuthenticationEntryPoint;
import com.my.app.jwt.JwtFilter;
import com.my.app.service.CustomOAuth2UserService;
import com.my.app.service.JwtUtils;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	private final JwtUtils jwtUtils;
	//private final UserDetailsService userDetailsService;
	//private final PasswordEncoder passwordEncoder;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CorsFilter corsFilter;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final AuthenticationSuccessHandler authenticationSuccessHandler;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/favicon.ico", "/error", "/**/*.js", "/**/*.css");
	}

//	@Bean
//	public DaoAuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//
//		authProvider.setUserDetailsService(userDetailsService);
//		authProvider.setPasswordEncoder(passwordEncoder());
//
//		return authProvider;
//	}

//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//		return authConfig.getAuthenticationManager();
//	}

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    	logger.info("========================================================");
    	logger.info("SecurityConfig filterChain ??????");
    	logger.info("========================================================");
        httpSecurity
                // token??? ???????????? ???????????? ????????? csrf??? disable
        		.cors().and().csrf().disable()
                // ??????????????????????????? ??????
               // .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
        		.authorizeRequests()
        		.antMatchers("/comm/**").permitAll()
        		.antMatchers("/user/**").permitAll()
        		.antMatchers("/api/**").permitAll()
        		.antMatchers("/login/**").permitAll()
        		.antMatchers("/oauth2/**").permitAll()
        		.anyRequest().authenticated();        

        httpSecurity
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // ????????????
                .accessDeniedHandler(jwtAccessDeniedHandler); // ????????????   
		                
        // ????????? ???????????? ?????? ????????? STATELESS??? ??????
      	httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//                httpSecurity.authenticationProvider(authenticationProvider());
                
        httpSecurity
        	.formLogin()
        	.loginPage("/comm/loginPage")
        	.usernameParameter("id")	//????????? ????????????????????? username??? ?????? ????????? ?????? ????????? ??????????????? ?????? ?????? ??????
			.passwordParameter("password")	//?????? password??? ??????
        	.permitAll();
        
        httpSecurity
        	.oauth2Login()
        	.defaultSuccessUrl("/user/myPage")	
        	.userInfoEndpoint()			// ????????? ?????? ??? ?????????????????? ????????????
            .userService(customOAuth2UserService)	//?????????????????? ????????? ??? ????????????	       
	        .and()
	        .successHandler(authenticationSuccessHandler);
	        //.failureHandler(authenticationFailureHandler);
	            
        httpSecurity
            // JwtAuthenticationFilter??? UsernamePasswordAuthenticationFilter ?????? ?????????
            .addFilterBefore(new JwtFilter(jwtUtils),
                    UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}