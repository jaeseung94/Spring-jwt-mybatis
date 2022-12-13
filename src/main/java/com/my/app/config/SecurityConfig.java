package com.my.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.my.app.jwt.JwtAccessDeniedHandler;
import com.my.app.jwt.JwtAuthenticationEntryPoint;
import com.my.app.jwt.JwtFilter;
//import com.my.app.jwt.JwtSecurityConfig;
import com.my.app.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	private final  TokenProvider tokenProvider;
	private final  CorsFilter corsFilter;
	private final  JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/favicon.ico", "/error","/**/*.js", "/**/*.css");
	}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    	logger.info("========================================================");
    	logger.info("SecurityConfig filterChain 실행");
    	logger.info("========================================================");
        httpSecurity
                // token을 사용하는 방식이기 때문에 csrf를 disable
                .csrf().disable()
                // 교차출처리소스공유 설정
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증실패
                .accessDeniedHandler(jwtAccessDeniedHandler) // 권한실패
                
                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/comm/**").permitAll()
                .antMatchers("/user/**").permitAll()
//                .antMatchers("/api/authenticate").permitAll()
//                .antMatchers("/api/signup").permitAll()
//                .antMatchers("/api/index").permitAll()
//                .antMatchers("/api/loginPage").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                	.loginPage("/comm/loginPage")
//                	.loginProcessingUrl("/api/authenticate")
//                	.successHandler(customAuthenticationSuccessHandler)
//                	.failureHandler(customAuthenticationFailureHandler)
                	.usernameParameter("id")	//스프링 시큐리티에서는 username을 기본 아이디 매핑 값으로 사용하는데 이거 쓰면 변경
    				.passwordParameter("password")	//이건 password를 변경
                	.permitAll()
                .and()
                // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다
                .addFilterBefore(new JwtFilter(tokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}