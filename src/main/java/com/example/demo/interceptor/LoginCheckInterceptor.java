package com.example.demo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.model.AccountLogin;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

	@Autowired
	private AccountLogin accountLogin;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (accountLogin.getId() == null) {
			response.sendRedirect(request.getContextPath() + "/");
			return false; // コントローラーの処理を中断
		}

		return true;

	}
}
