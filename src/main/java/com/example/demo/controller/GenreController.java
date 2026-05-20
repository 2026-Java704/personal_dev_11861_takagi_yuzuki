package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Genre;
import com.example.demo.repository.GenreRepository;

@Controller
public class GenreController {
	private final GenreRepository genreRepository;

	public GenreController(GenreRepository genreRepository) {
		this.genreRepository = genreRepository;
	}

	@GetMapping("/genres")
	public String index(Model model) {
		List<Genre> genreList = genreRepository.findAll();
		model.addAttribute("genres", genreList);
		return "genre/genres";
	}

	@GetMapping("/genres/add")
	public String add() {
		return "genre/addGenre";
	}

	@PostMapping("/genres/add")
	public String store(@RequestParam(defaultValue = "") String genreName,
			@RequestParam boolean isIncome,
			Model model) {

		if (genreName.equals("")) {
			model.addAttribute("errer", "ジャンル名は必須です");
			return "genre/addGenre";
		}

		Genre genre = new Genre(genreName, isIncome);
		genreRepository.save(genre);

		return "redirect:/genres";
	}

	@GetMapping("/genres/{id}/edit")
	public String edit(@PathVariable Integer id,
			Model model) {
		Genre genre = genreRepository.findById(id).get();
		model.addAttribute("genre", genre);
		return "genre/editGenre";
	}

	@PostMapping("/genres/{id}/edit")
	public String update(@PathVariable Integer id,
			@RequestParam String genreName,
			@RequestParam boolean isIncome) {

		Genre genre = genreRepository.findById(id).get();
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
