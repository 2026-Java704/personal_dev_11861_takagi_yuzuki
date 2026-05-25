package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Budget;
import com.example.demo.entity.Genre;
import com.example.demo.entity.User;
import com.example.demo.model.AccountLogin;
import com.example.demo.repository.BudgetRepository;
import com.example.demo.repository.GenreRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BudgetService;
import com.example.demo.service.NowService;

@Controller
public class BudgetController {

	private final BudgetRepository budgetRepository;
	private final ItemRepository itemRepository;
	private final GenreRepository genreRepository;
	private final UserRepository userRepository;
	private final AccountLogin accountLogin;
	private final BudgetService budgetService;
	private final NowService now;

	public BudgetController(BudgetRepository budgetRepository,
			ItemRepository itemRepository,
			GenreRepository genreRepository,
			UserRepository userRepository,
			AccountLogin accountLogin,
			BudgetService budgetService,
			NowService now) {
		this.budgetRepository = budgetRepository;
		this.itemRepository = itemRepository;
		this.genreRepository = genreRepository;
		this.userRepository = userRepository;
		this.accountLogin = accountLogin;
		this.budgetService = budgetService;
		this.now = now;
	}

	// 予算と実績の結果表示（何もないなら何もないで）
	@GetMapping("/budgets")
	public String index(Model model) {
		now.nowYearMonthDate(model);
		List<Budget> budgetList = budgetRepository.findByOrderByYearDescMonthDesc();

		for (Budget budget : budgetList) {
			budget.setAchievement(budgetService.achievement(accountLogin.getId(), budget.getGenre().getId(),
					budget.getYear(), budget.getMonth(), budget.getAmount()));
			budgetRepository.save(budget);
		}

		model.addAttribute("budgets", budgetList);
		return "budget/budgets";
	};

	// 予算設定
	@GetMapping("/budgets/add")
	public String add(Model model) {
		model.addAttribute("genres", genreRepository.findByIsIncome(false));
		model.addAttribute("budget", new Budget());
		return "budget/addBudget";
	};

	// 予算設定処理
	@PostMapping("/budgets/add")
	public String store(@RequestParam(defaultValue = "") Integer year,
			@RequestParam(defaultValue = "") Integer month,
			@RequestParam(defaultValue = "") Integer genreId,
			@RequestParam(defaultValue = "") Integer amount,
			Model model) {

		User user = userRepository.findById(accountLogin.getId()).get();
		Genre genre = genreRepository.findById(genreId).get();
		List<String> errerList = new ArrayList<>();

		if (year == null) {
			errerList.add("西暦を入力してください");
		}
		if (month != null && month < 1 || month > 12) {
			errerList.add("1～12以外の数字で入力してください");
		}
		if (amount == null) {
			errerList.add("予算を入力してください");
		}

		Budget budget = new Budget(user, genre, year, month, amount);

		if (errerList.isEmpty()) {
			budgetRepository.save(budget);
			return "redirect:/budgets";

		} else {
			model.addAttribute("errers", errerList);
			model.addAttribute("genres", genreRepository.findByIsIncome(false));
			model.addAttribute("budget", budget);
			return "budget/addBudget";
		}

	};

	// 予算編集
	@GetMapping("/budgets/{id}/edit")
	public String edit(@PathVariable Integer id,
			Model model) {
		Budget budget = budgetRepository.findById(id).get();
		model.addAttribute("budget", budget);
		return "budget/editBudget";
	};

	// 予算編集処理
	@PostMapping("/budgets/{id}/edit")
	public String update(@PathVariable Integer id) {
		return "redirect:/budgets";
	};

	// 予算削除
	@PostMapping("/budgets/{id}/delete")
	public String delete(@PathVariable Integer id) {
		budgetRepository.deleteById(id);
		return "redirect:/budgets";
	};

}
