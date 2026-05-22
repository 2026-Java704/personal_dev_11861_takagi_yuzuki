package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private LoginCheckInterceptor loginCheckInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// ログイン画面や静的リソースを除外してインターセプターを適用
		registry.addInterceptor(loginCheckInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/", "/login", "/logout", "/css/**");
	}
}
