package com.example.demo.controller;

import java.time.LocalDate;
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

@Controller
public class ItemController {

	private final ItemRepository itemRepository;
	private final UserRepository userRepository;
	private final AccountLogin accountLogin;
	private final GenreRepository genreRepository;

	public ItemController(ItemRepository itemRepository,
			UserRepository userRepository,
			AccountLogin accountLogin,
			GenreRepository genreRepository) {
		this.itemRepository = itemRepository;
		this.userRepository = userRepository;
		this.accountLogin = accountLogin;
		this.genreRepository = genreRepository;
	}

	@GetMapping("/items")
	public String index(@RequestParam(defaultValue = "") Integer genreId,
			Model model) {
		List<Item> itemList = itemRepository.findAll();
		model.addAttribute("items", itemList);
		return "item/items";
	}

	@GetMapping("/items/add")
	public String add() {
		return "item/addEditItem";
	}

	@PostMapping("/items/add")
	public String store(@RequestParam(defaultValue = "") LocalDate addDate,
			@RequestParam(defaultValue = "") Integer genreId,
			@RequestParam(defaultValue = "") Integer price,
			@RequestParam(defaultValue = "") String itemName,
			@RequestParam(defaultValue = "") String comment) {

		User user = userRepository.findById(accountLogin.getId()).get();
		Genre genre = genreRepository.findById(genreId).get();

		Item item = new Item(itemName, user, genre, price, addDate, comment);

		itemRepository.save(item);

		return "redirect:/items";
	}

	@GetMapping("/items/{id}/edit")
	public String edit(@PathVariable Integer id,
			Model model) {
		Item item = itemRepository.findById(id).get();
		model.addAttribute("item", item);
		return "item/addEditItem";
	}

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

		item.update(itemName, genre, price, addDate, comment);

		itemRepository.save(item);

		return "redirect:/items";
	}

	@GetMapping("/items/{id}/delete")
	public String delete(@PathVariable Integer id,
			Model model) {
		itemRepository.deleteById(id);
		return "redirect:/items";
	}

}
