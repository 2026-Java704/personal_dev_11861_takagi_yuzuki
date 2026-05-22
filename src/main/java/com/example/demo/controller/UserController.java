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
import com.example.demo.service.NowService;

@Controller
public class UserController {

	private final UserRepository userRepository;
	private final HttpSession session;
	private final AccountLogin accountLogin;
	private final NowService now;

	public UserController(UserRepository userRepository,
			HttpSession session,
			AccountLogin accountLogin,
			NowService now) {
		this.userRepository = userRepository;
		this.session = session;
		this.accountLogin = accountLogin;
		this.now = now;
	}

	// 新規登録画面表示
	@GetMapping("/register")
	public String register(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "user/create";
	}

	// 新規登録処理
	@PostMapping("/register")
	public String add(@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "") String email,
			@RequestParam(defaultValue = "") String password,
			@RequestParam(defaultValue = "") String passwordConfirm,
			Model model) {

		List<String> errerList = new ArrayList<>();
		User user = userRepository.findByEmail(email);

		if (name.equals("")) {
			errerList.add("名前は必須です");
		}
		if (name.length() > 20) {
			errerList.add("名前は20文字以内で入力してください");
		}
		if (email.equals("")) {
			errerList.add("メールアドレスは必須です");
		}
		if (password.equals("")) {
			errerList.add("パスワードは必須です");
		} else if (password.length() < 6 || password.length() > 16) {
			errerList.add("パスワードは6文字以上、16文字以内で入力してください");
		} else if (!password.equals(passwordConfirm)) {
			errerList.add("パスワードが違っています");
		}

		if (user != null) {
			errerList.add("すでに登録されてるメールアドレスです");
		}

		user = new User(name, email, password);

		if (errerList.isEmpty()) {
			userRepository.save(user);
			return "redirect:/";
		} else {
			model.addAttribute("user", user);
			model.addAttribute("errers", errerList);
			return "user/create";
		}
	}

	// ログイン画面表示
	@GetMapping({ "/", "/login" })
	public String index() {
		return "user/login";
	}

	// ログイン処理
	@PostMapping("/login")
	public String login(@RequestParam(defaultValue = "") String email,
			@RequestParam(defaultValue = "") String password,
			Model model) {

		User user = userRepository.findByEmailAndPassword(email, password);

		if (user == null) {
			model.addAttribute("errer", "ユーザーが見つかりませんでした");
			return "user/login";
		} else {
			accountLogin.setId(user.getId());
			accountLogin.setName(user.getUserName());
			session.setAttribute("loginUser", user);
			return "redirect:/items";
		}
	}

	// ログアウト処理
	@GetMapping("/logout")
	public String logaut() {
		session.invalidate();
		return "user/logout";
	}

	// アカウント情報画面表示
	@GetMapping("/account")
	public String account(Model model) {
		if (accountLogin.getId() == null) {
			return "redirect:/";
		}
		now.nowYearMonthDate(model);
		User user = userRepository.findById(accountLogin.getId()).get();
		model.addAttribute("user", user);
		return "user/user";
	}

	@GetMapping("/account/edit")
	public String edit(Model model) {
		now.nowYearMonthDate(model);
		User user = userRepository.findById(accountLogin.getId()).get();
		model.addAttribute("user", user);
		return "user/editUser";
	}

	@PostMapping("/account/edit")
	public String update(@RequestParam(defaultValue = "") String userName,
			@RequestParam(defaultValue = "") String email,
			@RequestParam(defaultValue = "") String password,
			@RequestParam(defaultValue = "") String passwordConfirm,
			Model model) {
		now.nowYearMonthDate(model);
		List<String> errerList = new ArrayList<>();
		User user = userRepository.findById(accountLogin.getId()).get();

		if (userName.equals("")) {
			errerList.add("名前が未入力です");
		}
		if (email.equals("")) {
			errerList.add("メールアドレスが未入力です");
		}
		if (password.equals("") && passwordConfirm.equals("")) {
			password = user.getPassword();
		} else if (password.equals(passwordConfirm)) {
			errerList.add("パスワードが一致しませんでした");
		}

		if (errerList.isEmpty()) {
			user.update(userName, email, password);
			userRepository.save(user);
			return "redirect:/account";
		} else {
			model.addAttribute("errers", errerList);
			model.addAttribute("user", user);
			return "user/editUser";
		}

	}

	@PostMapping("/account/delete")
	public String delete() {
		userRepository.deleteById(accountLogin.getId());
		return "redirect:/";
	}

}
