package com.example.demo.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

// 現在時間取得

@Service
public class NowService {

	public LocalDate now() {
		LocalDate now = LocalDate.now();
		return now;
	}

	public void nowYearMonthDate(Model model) {
		model.addAttribute("year", now().getYear());
		model.addAttribute("month", now().getMonthValue());
	}

}
