package com.example.demo.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Item;
import com.example.demo.repository.BudgetRepository;
import com.example.demo.repository.ItemRepository;

@Service
public class BudgetService {
	private final BudgetRepository budgetRepository;
	private final ItemRepository itemRepository;

	public BudgetService(BudgetRepository budgetRepository,
			ItemRepository itemRepository) {
		this.budgetRepository = budgetRepository;
		this.itemRepository = itemRepository;
	}

	public Integer achievement(Integer userId, Integer genreId, Integer year, Integer month, Integer amount) {
		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate startDate = yearMonth.atDay(1);
		LocalDate endDate = yearMonth.atEndOfMonth();

		List<Item> itemList = null;
		if (genreId == null) {
			itemList = itemRepository.findByUser_IdAndAddDateBetweenOrderByAddDate(userId, startDate, endDate);
		} else {
			itemList = itemRepository.findByUser_IdAndGenre_IdAndAddDateBetweenOrderByAddDate(userId, genreId,
					startDate, endDate);
		}
		int itemPrice = 0;
		for (Item item : itemList) {
			if (item.getGenre().getIsIncome() == false) {
				itemPrice += item.getPrice();
			}
		}

		int achievement = amount - itemPrice;

		return achievement;
	}

}
