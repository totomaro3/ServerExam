package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.interceptor.BeforeActionInterceptor;
import com.example.demo.interceptor.NeedLoginIntercepter;
import com.example.demo.interceptor.NeedLogoutIntercepter;



@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
	// BeforeActionInterceptor 불러오기
	@Autowired
	BeforeActionInterceptor beforeActionInterceptor;
	
	@Autowired
	NeedLoginIntercepter needLoginInterceptor;
	
	@Autowired
	NeedLogoutIntercepter needLogoutIntercepter;

	// /resource/common.css
	// 인터셉터 적용
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(beforeActionInterceptor)
		.addPathPatterns("/**")
		.addPathPatterns("/favicon.ico")
		.excludePathPatterns("/resource/**").excludePathPatterns("/error");
		
		registry.addInterceptor(needLoginInterceptor)
		.addPathPatterns("/usr/member/doLogout")
		.excludePathPatterns("/resource/**").excludePathPatterns("/error");
		
		registry.addInterceptor(needLogoutIntercepter)
		.addPathPatterns("/usr/member/login")
		.addPathPatterns("/usr/member/doLogin")
		.addPathPatterns("/usr/member/getLoginIdDup")
		.addPathPatterns("/usr/member/getEmailDup")
		.addPathPatterns("/usr/member/getLoginPwConfirm")
		.addPathPatterns("/usr/member/join")
		.addPathPatterns("/usr/member/doJoin")
		.excludePathPatterns("/resource/**").excludePathPatterns("/error");
	}

}