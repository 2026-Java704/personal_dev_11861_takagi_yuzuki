package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.model.AccountLogin;
import com.example.demo.repository.UserRepository;

@Controller
public class UserController {
	private final UserRepository userRepository;
	private final HttpSession session;
	private final AccountLogin accountLogin;

	public UserController(UserRepository userRepository,
			HttpSession session,
			AccountLogin accountLogin) {
		this.userRepository = userRepository;
		this.session = session;
		this.accountLogin = accountLogin;
	}

	@GetMapping("/register")
	public String register() {
		return "user/create";
	}

	@PostMapping("/register")
	public String add(@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "") String email,
			@RequestParam(defaultValue = "") String password,
			@RequestParam(defaultValue = "") String passwordConfirm,
			Model model) {

		List<String> errerList = new ArrayList<>();
		User userEmail = userRepository.findByEmail(email);

		if (name.equals("")) {
			errerList.add("名前は必須です");
		}
		if (email.equals("")) {
			errerList.add("メールアドレスは必須です");
		}
		if (password.equals("")) {
			errerList.add("パスワードは必須です");
		}
		if (!password.equals(passwordConfirm)) {
			errerList.add("パスワードが違っています");
		}
		if (userEmail != null) {
			errerList.add("すでに登録されてるメールアドレスです");
		}

		if (errerList.isEmpty()) {
			User user = new User(name, email, password);
			userRepository.save(user);
			return "redirect:/";
		} else {
			model.addAttribute("errers", errerList);
			return "user/create";
		}
	}

	@GetMapping({ "/", "/login" })
	public String index() {
		session.invalidate();
		return "user/login";
	}

	@PostMapping("/login")
	public String login(@RequestParam(defaultValue = "") String email,
			@RequestParam(defaultValue = "") String password,
			Model model) {

		User user = userRepository.findByEmailAndPassword(email, password);

		if (user == null) {
			model.addAttribute("errerMessage", "ユーザーが見つかりませんでした");
			return "user/login";
		} else {
			accountLogin.setId(user.getId());
			accountLogin.setName(user.getUserName());
			return "redirect:/items";
		}
	}

}
