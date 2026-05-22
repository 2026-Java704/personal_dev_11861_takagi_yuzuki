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

import com.example.demo.entity.Genre;
import com.example.demo.entity.Item;
import com.example.demo.entity.User;
import com.example.demo.model.AccountLogin;
import com.example.demo.repository.GenreRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ItemService;
import com.example.demo.service.NowService;

@Controller
public class ItemController {

	private final ItemService itemService;
	private final ItemRepository itemRepository;
	private final UserRepository userRepository;
	private final AccountLogin accountLogin;
	private final GenreRepository genreRepository;
	private final NowService now;

	public ItemController(ItemRepository itemRepository,
			UserRepository userRepository,
			AccountLogin accountLogin,
			GenreRepository genreRepository,
			ItemService itemService,
			NowService now) {
		this.itemRepository = itemRepository;
		this.userRepository = userRepository;
		this.accountLogin = accountLogin;
		this.genreRepository = genreRepository;
		this.itemService = itemService;
		this.now = now;
	}

	// ジャンル一覧
	public void genreList(Model model) {
		List<Genre> genreList = genreRepository.findAll();
		model.addAttribute("genres", genreList);
	}

	// 一覧表示（リスト型）
	@GetMapping("/items")
	public String index(@RequestParam(defaultValue = "") Integer genreId,
			@RequestParam(defaultValue = "") Integer year,
			@RequestParam(defaultValue = "") Integer month,
			Model model) {
		genreList(model);
		now.nowYearMonthDate(model);

		List<Item> itemList = itemRepository.findByUser_IdOrderByAddDate(accountLogin.getId());
		if (genreId != null) {
			itemList = itemRepository.findByUser_IdAndGenre_IdOrderByAddDate(accountLogin.getId(), genreId);
		}

		model.addAttribute("items", itemList);

		model.addAttribute("totalMonth",
				itemService.getMonthTotal(accountLogin.getId(), now.now().getYear(), now.now().getMonthValue()));
		model.addAttribute("totalYear",
				itemService.getYearTotal(accountLogin.getId(), now.now().getYear()));

		return "item/items";
	}

	// 追加画面表示
	@GetMapping("/items/add")
	public String add(Model model) {
		now.nowYearMonthDate(model);
		Item item = new Item();
		genreList(model);
		model.addAttribute("item", item);
		return "item/addItem";
	}

	// 追加処理
	@PostMapping("/items/add")
	public String store(@RequestParam(defaultValue = "") LocalDate addDate,
			@RequestParam(defaultValue = "") Integer genreId,
			@RequestParam(defaultValue = "") Integer price,
			@RequestParam(defaultValue = "") String itemName,
			@RequestParam(defaultValue = "") String comment,
			Model model) {

		List<String> errerList = new ArrayList<>();

		if (addDate == null) {
			errerList.add("日付を選択してください");
		}
		if (price == null) {
			errerList.add("金額を入力してください");
		}
		if (itemName.equals("")) {
			errerList.add("名前を入力してください");
		}

		if (errerList.isEmpty()) {
			User user = userRepository.findById(accountLogin.getId()).get();
			Genre genre = genreRepository.findById(genreId).get();
			Item item = new Item(itemName, user, genre, price, addDate, comment);
			itemRepository.save(item);
			return "redirect:/items";

		} else {
			model.addAttribute("errers", errerList);
			now.nowYearMonthDate(model);
			return "item/addItem";
		}
	}

	// 更新画面表示
	@GetMapping("/items/{id}/edit")
	public String edit(@PathVariable Integer id,
			Model model) {
		now.nowYearMonthDate(model);
		genreList(model);
		Item item = itemRepository.findById(id).get();
		model.addAttribute("item", item);
		return "item/editItem";
	}

	// 更新処理
	@PostMapping("/items/{id}/edit")
	public String update(@PathVariable Integer id,
			@RequestParam(defaultValue = "") String itemName,
			@RequestParam(defaultValue = "") LocalDate addDate,
			@RequestParam(defaultValue = "") Integer genreId,
			@RequestParam(defaultValue = "") Integer price,
			@RequestParam(defaultValue = "") String comment,
			Model model) {
		Item item = itemRepository.findById(id).get();
		Genre genre = genreRepository.findById(genreId).get();

		List<String> errerList = new ArrayList<>();

		if (addDate == null) {
			errerList.add("月日を選択してください");
		}
		if (itemName.equals("")) {
			errerList.add("名前を入力してください");
		}
		if (price == null) {
			errerList.add("金額を入力してください");
		}

		if (errerList.isEmpty()) {
			item.update(itemName, genre, price, addDate, comment);
			itemRepository.save(item);
			return "redirect:/items";
		} else {
			model.addAttribute("errers", errerList);
			model.addAttribute("item", item);
			now.nowYearMonthDate(model);
			return "item/editItem";
		}
	}

	// 削除処理
	@PostMapping("/items/{id}/delete")
	public String delete(@PathVariable Integer id,
			Model model) {
		itemRepository.deleteById(id);
		return "redirect:/items";
	}

	// カレンダー
	@GetMapping("/calendar/{year}/{month}")
	public String showCalendar(@PathVariable(value = "year", required = false) Integer year,
			@PathVariable(value = "month", required = false) Integer month,
			Model model) {

		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate firstOfMonth = yearMonth.atDay(1); // 月初

		//指定された年月の前月・次月を取得
		LocalDate prevMonth = firstOfMonth.minusMonths(1);
		LocalDate nextMonth = firstOfMonth.plusMonths(1);

		List<Item> itemList = itemRepository.findByUser_IdAndAddDateBetweenOrderByAddDate(accountLogin.getId(),
				firstOfMonth, yearMonth.atEndOfMonth());

		// カレンダーの開始日（月初の日曜日の日付）を算出
		int offset = firstOfMonth.getDayOfWeek().getValue() % 7;
		LocalDate startDate = firstOfMonth.minusDays(offset);

		// 42マス分の日付リストを作成
		List<LocalDate> calendarDates = new ArrayList<>();
		for (int i = 0; i < 42; i++) {
			calendarDates.add(startDate.plusDays(i));
		}

		model.addAttribute("calendarDates", calendarDates);
		yesrMonth(year, month, model);

		model.addAttribute("items", itemList);

		prevNext(prevMonth, nextMonth, model);

		model.addAttribute("totalMonth",
				itemService.getMonthTotal(accountLogin.getId(), firstOfMonth.getYear(), firstOfMonth.getMonthValue()));

		return "item/calendar";
	}

	@GetMapping("/detil/{year}/{month}")
	public String detil(@PathVariable(value = "year", required = false) Integer year,
			@PathVariable(value = "month", required = false) Integer month,
			Model model) {

		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate firstOfMonth = yearMonth.atDay(1); // 月初

		//指定された年月の前月・次月を取得
		LocalDate prevMonth = firstOfMonth.minusMonths(1);
		LocalDate nextMonth = firstOfMonth.plusMonths(1);

		// 収入
		int totalIncomeMonth = itemService.getMonthIncome(accountLogin.getId(), firstOfMonth.getYear(),
				firstOfMonth.getMonthValue());
		// 支出
		int totalExpenseMonth = itemService.getMonthExpense(accountLogin.getId(), firstOfMonth.getYear(),
				firstOfMonth.getMonthValue());
		// 収支
		int totalMonth = itemService.getMonthTotal(accountLogin.getId(), firstOfMonth.getYear(),
				firstOfMonth.getMonthValue());

		// ジャンルごとの合計金額とパーセント
		List<Genre> genreList = genreRepository.findAll();
		for (Genre genre : genreList) {
			genre.setTotal(itemService.genreTotal(accountLogin.getId(), genre.getId(), firstOfMonth.getYear(),
					firstOfMonth.getMonthValue()));
			if (genre.getIsIncome() == true) {
				genre.setPercent(itemService.genrePercent(accountLogin.getId(), genre.getId(), totalIncomeMonth,
						firstOfMonth.getYear(),
						firstOfMonth.getMonthValue()));
			} else {
				genre.setPercent(itemService.genrePercent(accountLogin.getId(), genre.getId(), totalExpenseMonth,
						firstOfMonth.getYear(),
						firstOfMonth.getMonthValue()));
			}
			genreRepository.save(genre);
		}

		// グラフ用
		List<Integer> percents = new ArrayList<>();
		List<String> genreLabels = new ArrayList<>();
		for (Genre genre : genreList) {
			if (genre.getIsIncome() == false) {
				genreLabels.add(genre.getGenreName());
				percents.add(genre.getPercent());
			}
		}

		genreList(model);
		model.addAttribute("labels", genreLabels);
		model.addAttribute("values", percents);

		model.addAttribute("totalIncomeMonth", totalIncomeMonth);
		model.addAttribute("totalExpenseMonth", totalExpenseMonth);
		model.addAttribute("totalMonth", totalMonth);

		yesrMonth(year, month, model);
		prevNext(prevMonth, nextMonth, model);

		return "item/detil";
	}

	// 年月
	public void yesrMonth(int year, int month, Model model) {
		model.addAttribute("currentYear", year);
		model.addAttribute("currentMonth", month);
	}

	// 翌年翌月・昨年昨月
	public void prevNext(LocalDate prevMonth, LocalDate nextMonth, Model model) {
		model.addAttribute("prevYear", prevMonth.getYear());
		model.addAttribute("prevMonth", prevMonth.getMonthValue());
		model.addAttribute("nextYear", nextMonth.getYear());
		model.addAttribute("nextMonth", nextMonth.getMonthValue());
	}

	@GetMapping("/comparison/{year}")
	public String a(@PathVariable Integer year,
			Model model) {

		now.nowYearMonthDate(model);

		List<Integer> totalIncome = new ArrayList<>();
		List<Integer> totalExpense = new ArrayList<>();
		List<Integer> total = new ArrayList<>();

		for (int i = 1; i <= 12; i++) {
			YearMonth yearMonth = YearMonth.of(year, i);
			LocalDate firstOfMonth = yearMonth.atDay(1);

			// 収入
			totalIncome.add(itemService.getMonthIncome(accountLogin.getId(), firstOfMonth.getYear(),
					firstOfMonth.getMonthValue()));
			// 支出
			totalExpense.add(itemService.getMonthExpense(accountLogin.getId(), firstOfMonth.getYear(),
					firstOfMonth.getMonthValue()));
			// 収支
			total.add(itemService.getMonthTotal(accountLogin.getId(), firstOfMonth.getYear(),
					firstOfMonth.getMonthValue()));
		}

		model.addAttribute("totalIncome", totalIncome);
		model.addAttribute("totalExpense", totalExpense);
		model.addAttribute("total", total);

		return "item/comparison";
	}

}
