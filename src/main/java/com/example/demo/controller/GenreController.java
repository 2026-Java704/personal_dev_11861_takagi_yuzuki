package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Genre;
import com.example.demo.model.AccountLogin;
import com.example.demo.repository.GenreRepository;
import com.example.demo.service.NowService;

@Controller
public class GenreController {
	private final GenreRepository genreRepository;
	private final NowService now;
	private final AccountLogin accountLogin;

	public GenreController(GenreRepository genreRepository,
			AccountLogin accountLogin,
			NowService now) {
		this.genreRepository = genreRepository;
		this.now = now;
		this.accountLogin = accountLogin;
	}

	@GetMapping("/genres")
	public String index(Model model) {
		now.nowYearMonthDate(model);
		//		if (accountLogin.getId() == null) {
		//			return "redirect:/";
		//		}
		List<Genre> genreList = genreRepository.findAllByOrderById();
		model.addAttribute("genres", genreList);
		return "genre/genres";
	}

	@GetMapping("/genres/add")
	public String add(Model model) {
		now.nowYearMonthDate(model);
		Genre genre = new Genre("", true);
		model.addAttribute("genre", genre);
		return "genre/addGenre";
	}

	@PostMapping("/genres/add")
	public String store(@RequestParam(defaultValue = "") String genreName,
			@RequestParam boolean isIncome,
			Model model) {

		Genre genre = new Genre(genreName, isIncome);

		if (genreName.equals("")) {
			model.addAttribute("errer", "ジャンル名は必須です");
			now.nowYearMonthDate(model);
			model.addAttribute("genre", genre);
			return "genre/addGenre";
		}

		genreRepository.save(genre);

		return "redirect:/genres";
	}

	@GetMapping("/genres/{id}/edit")
	public String edit(@PathVariable Integer id,
			Model model) {
		now.nowYearMonthDate(model);
		Genre genre = genreRepository.findById(id).get();
		model.addAttribute("genre", genre);
		return "genre/editGenre";
	}

	@PostMapping("/genres/{id}/edit")
	public String update(@PathVariable Integer id,
			@RequestParam String genreName,
			@RequestParam boolean isIncome,
			Model model) {

		Genre genre = genreRepository.findById(id).get();

		if (genreName.equals("")) {
			model.addAttribute("errer", "ジャンル名は必須です");
			now.nowYearMonthDate(model);
			return "genre/addGenre";
		}

		genre.update(genreName, isIncome);
		genreRepository.save(genre);

		return "redirect:/genres";
	}

	@PostMapping("/genres/{id}/delete")
	public String delete(@PathVariable Integer id) {
		genreRepository.deleteById(id);
		return "redirect:/genres";
	}

}
