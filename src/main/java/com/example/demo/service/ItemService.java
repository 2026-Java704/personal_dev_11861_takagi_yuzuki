package com.example.demo.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Item;
import com.example.demo.repository.ItemRepository;

// 収支処理

@Service
public class ItemService {
	private final ItemRepository itemRepository;

	public ItemService(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	// 月の収支合計（収支）を計算する
	public Integer getMonthTotal(int id, int year, int month) {
		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate startDate = yearMonth.atDay(1);
		LocalDate endDate = yearMonth.atEndOfMonth();

		List<Item> itemList = itemRepository.findByUser_IdAndAddDateBetweenOrderByAddDate(id, startDate, endDate);

		// 収入
		int totalIncome = 0;

		// 支出
		int totalExpense = 0;

		for (Item item : itemList) {
			if (item.getGenre().getIsIncome()) {
				totalIncome += item.getPrice();
			} else {
				totalExpense += item.getPrice();
			}
		}

		// 収支
		int total = totalIncome - totalExpense;

		return total;
	}

	// 月の収支合計（収入）を計算する
	public Integer getMonthIncome(int id, int year, int month) {
		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate startDate = yearMonth.atDay(1);
		LocalDate endDate = yearMonth.atEndOfMonth();

		List<Item> itemList = itemRepository.findByUser_IdAndAddDateBetweenOrderByAddDate(id, startDate, endDate);

		// 収入
		int income = 0;

		for (Item item : itemList) {
			if (item.getGenre().getIsIncome()) {
				income += item.getPrice();
			}
		}

		return income;
	}

	// 月の収支合計（支出）を計算する
	public Integer getMonthExpense(int id, int year, int month) {
		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate startDate = yearMonth.atDay(1);
		LocalDate endDate = yearMonth.atEndOfMonth();

		List<Item> itemList = itemRepository.findByUser_IdAndAddDateBetweenOrderByAddDate(id, startDate, endDate);

		// 支出
		int expense = 0;

		for (Item item : itemList) {
			if (!item.getGenre().getIsIncome()) {
				expense += item.getPrice();
			}
		}

		return expense;
	}

	// 今年の収支合計（収入、支出、利益）を計算する
	public Integer getYearTotal(int id, int year) {
		LocalDate today = LocalDate.now();
		LocalDate startDate = today.withDayOfYear(1);
		LocalDate endDate = today.withDayOfYear(today.lengthOfYear());

		List<Item> itemList = itemRepository.findByUser_IdAndAddDateBetweenOrderByAddDate(id, startDate, endDate);

		// 収入
		int totalIncome = 0;

		// 支出
		int totalExpense = 0;

		for (Item item : itemList) {
			if (item.getGenre().getIsIncome()) {
				totalIncome += item.getPrice();
			} else {
				totalExpense += item.getPrice();
			}
		}

		// 利益
		int total = totalIncome - totalExpense;

		return total;
	}

	// ジャンルごとの計算
	public Integer genreTotal(Integer userId, Integer genreId, int year, int month) {
		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate startDate = yearMonth.atDay(1);
		LocalDate endDate = yearMonth.atEndOfMonth();

		List<Item> itemList = itemRepository.findByUser_IdAndGenre_IdAndAddDateBetweenOrderByAddDate(userId, genreId,
				startDate, endDate);
		int totalGenre = 0;

		for (Item item : itemList) {
			totalGenre += item.getPrice();
		}

		return totalGenre;
	}

	// ジャンルごとのパーセント
	public Integer genrePercent(Integer userId, Integer genreId, int total, int year, int month) {
		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate startDate = yearMonth.atDay(1);
		LocalDate endDate = yearMonth.atEndOfMonth();

		List<Item> itemList = itemRepository.findByUser_IdAndGenre_IdAndAddDateBetweenOrderByAddDate(userId, genreId,
				startDate, endDate);
		int totalGenre = 0;

		for (Item item : itemList) {
			totalGenre += item.getPrice();
		}

		double percent = ((double) totalGenre / total * 100);
		totalGenre = (int) Math.round(percent);

		return totalGenre;
	}
}