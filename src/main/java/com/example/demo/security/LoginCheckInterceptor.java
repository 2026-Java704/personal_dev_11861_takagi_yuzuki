package com.example.demo.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession(false); // 既存セッションのみ取得

		// セッションがない、またはログイン情報（@SessionScopeのBeanなど）が消えている場合
		if (session == null) {
			response.sendRedirect(request.getContextPath() + "/");
			return false;
		}
		return true;
	}
}
