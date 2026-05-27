package com.example.demo.controller;

import java.time.LocalDate;
import java.time.YearMonth;
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
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BudgetService;
import com.example.demo.service.NowService;

@Controller
public class BudgetController {

	private final BudgetRepository budgetRepository;
	private final GenreRepository genreRepository;
	private final UserRepository userRepository;
	private final AccountLogin accountLogin;
	private final BudgetService budgetService;
	private final NowService now;

	public BudgetController(BudgetRepository budgetRepository,
			GenreRepository genreRepository,
			UserRepository userRepository,
			AccountLogin accountLogin,
			BudgetService budgetService,
			NowService now) {
		this.budgetRepository = budgetRepository;
		this.genreRepository = genreRepository;
		this.userRepository = userRepository;
		this.accountLogin = accountLogin;
		this.budgetService = budgetService;
		this.now = now;
	}

	// 予算と実績の結果表示（何もないなら何もないで）
	@GetMapping("/budgets/{year}/{month}")
	public String index(@PathVariable(value = "year", required = false) Integer year,
			@PathVariable(value = "month", required = false) Integer month,
			Model model) {
		List<Budget> budgetList = budgetRepository
				.findByUser_IdAndYearAndMonthOrderByYearDescMonthDesc(accountLogin.getId(), year, month);

		for (Budget budget : budgetList) {
			budget.setAchievement(budgetService.achievement(accountLogin.getId(), budget.getGenre().getId(),
					budget.getYear(), budget.getMonth(), budget.getAmount()));
			budgetRepository.save(budget);
		}

		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate firstOfMonth = yearMonth.atDay(1); // 月初

		//指定された年月の前月・次月を取得
		LocalDate prevMonth = firstOfMonth.minusMonths(1);
		LocalDate nextMonth = firstOfMonth.plusMonths(1);

		model.addAttribute("prevYear", prevMonth.getYear());
		model.addAttribute("prevMonth", prevMonth.getMonthValue());
		model.addAttribute("nextYear", nextMonth.getYear());
		model.addAttribute("nextMonth", nextMonth.getMonthValue());

		model.addAttribute("totalBudget",
				budgetService.totalBudget(accountLogin.getId(), year, month));
		model.addAttribute("totalPrice",
				budgetService.totalPrice(accountLogin.getId(), year, month));
		model.addAttribute("budgets", budgetList);
		System.out.println(budgetList);
		return "budget/budgets";
	};

	// 予算設定
	@GetMapping("/budgets/{year}/{month}/add")
	public String add(@PathVariable(value = "year", required = false) Integer year,
			@PathVariable(value = "month", required = false) Integer month,
			Model model) {
		model.addAttribute("genres", genreRepository.findByIsIncome(false));
		model.addAttribute("budget", new Budget(null, null, year, month, null));
		return "budget/addBudget";
	};

	// 予算設定処理
	@PostMapping("/budgets/add")
	public String store(@RequestParam(defaultValue = "") Integer year,
			@RequestParam(defaultValue = "") Integer month,
			@RequestParam(defaultValue = "") Integer genreId,
			@RequestParam(defaultValue = "") Integer amount,
			Model model) {

		List<String> errerList = new ArrayList<>();

		if (year == null) {
			errerList.add("西暦を入力してください");
			year = now.now().getYear();
		} else if (now.now().getYear() - 10 > year || now.now().getYear() + 10 < year) {
			errerList.add("西暦を正確に入力してください");
			year = now.now().getYear();
		}
		if (month == null) {
			errerList.add("月を入力してください");
			month = now.now().getMonthValue();
		} else if (month != null && month < 1 || month > 12) {
			errerList.add("1～12の数字で入力してください");
			month = now.now().getMonthValue();
		}
		if (genreId == null) {
			errerList.add("ジャンルを選択してください");
		}
		if (amount == null) {
			errerList.add("予算を入力してください");
		}

		User user = userRepository.findById(accountLogin.getId()).get();
		Genre genre = genreRepository.findById(genreId).get();
		Budget budget = new Budget(user, genre, year, month, amount);

		if (errerList.isEmpty()) {
			budgetRepository.save(budget);
			return "redirect:/budgets/" + year + "/" + month;

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
		model.addAttribute("genres", genreRepository.findByIsIncome(false));
		return "budget/editBudget";
	};

	// 予算編集処理
	@PostMapping("/budgets/{id}/edit")
	public String update(@PathVariable Integer id,
			@RequestParam(defaultValue = "") Integer year,
			@RequestParam(defaultValue = "") Integer month,
			@RequestParam(defaultValue = "") Integer amount,
			@RequestParam(defaultValue = "") Integer genreId,
			Model model) {

		Budget budget = budgetRepository.findById(id).get();
		List<String> errerList = new ArrayList<>();

		if (year == null) {
			errerList.add("西暦を入力してください");
			year = budget.getYear();
		} else if (now.now().getYear() - 10 > year || now.now().getYear() + 10 < year) {
			errerList.add("今年から10年前後までしか選べません");
			errerList.add("西暦を正確に入力してください");
			year = budget.getYear();
		}
		if (month == null) {
			errerList.add("月を入力してください");
			month = budget.getMonth();
		} else if (month != null && month < 1 || month > 12) {
			errerList.add("1～12の数字で入力してください");
			month = budget.getMonth();
		}
		if (genreId == null) {
			errerList.add("ジャンルを選択してください");
		}
		if (amount == null) {
			errerList.add("予算を入力してください");
		}

		User user = userRepository.findById(accountLogin.getId()).get();
		Genre genre = genreRepository.findById(genreId).get();
		budget.update(user, genre, year, month, amount);

		if (errerList.isEmpty()) {
			budgetRepository.save(budget);
			return "redirect:/budgets/" + year + "/" + month;
		} else {
			model.addAttribute("errers", errerList);
			model.addAttribute("budget", budget);
			model.addAttribute("genres", genreRepository.findByIsIncome(false));
			return "budget/editBudget";
		}

	};

	// 予算削除
	@PostMapping("/budgets/{id}/delete")
	public String delete(@PathVariable Integer id,
			@RequestParam Integer year,
			@RequestParam Integer month) {
		budgetRepository.deleteById(id);
		return "redirect:/budgets/" + year + "/" + month;
	};

}
