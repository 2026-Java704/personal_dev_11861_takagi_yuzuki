//package com.example.demo.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class LoginSecurity {
//
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http
//				.authorizeHttpRequests(auth -> auth
//						.requestMatchers("/login", "/logout", "/css/**", "/js/**").permitAll() // ログイン画面や静的リソースは認証不要
//						.anyRequest().authenticated() // 上記以外の全リクエストはログイン必須（未ログイン時はリダイレクト）
//				)
//				.formLogin(form -> form
//						.loginPage("/login") // ログイン画面のURL
//						.defaultSuccessUrl("/items") // ログイン成功時の遷移先
//						.permitAll());
//		return http.build();
//	}
//
//}
