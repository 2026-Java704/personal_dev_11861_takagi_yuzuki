package com.example.demo.controller;

import java.time.LocalDate;
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

	public void genreList(Model model) {
		List<Genre> genreList = genreRepository.findAll();
		model.addAttribute("genres", genreList);
	}

	@GetMapping("/items")
	public String index(@RequestParam(defaultValue = "") Integer genreId,
			Model model) {
		genreList(model);
		List<Item> itemList = itemRepository.findByUser_IdOrderByAddDate(accountLogin.getId());
		if (genreId != null) {
			itemList = itemRepository.findByUser_IdAndGenre_IdOrderByAddDate(accountLogin.getId(), genreId);
		}

		model.addAttribute("items", itemList);

		int total = 0;
		for (Item item : itemList) {
			if (item.getGenre().getIsIncome()) {
				total += item.getPrice();
			} else {
				total -= item.getPrice();
			}
		}
		model.addAttribute("total", total);

		return "item/items";
	}

	@GetMapping("/items/add")
	public String add(Model model) {
		Item item = new Item();
		genreList(model);
		model.addAttribute("item", item);
		model.addAttribute("onOff", true);
		return "item/addItem";
	}

	@PostMapping("/items/add")
	public String store(@RequestParam(defaultValue = "") LocalDate addDate,
			@RequestParam(defaultValue = "") Integer genreId,
			@RequestParam(defaultValue = "") Integer price,
			@RequestParam(defaultValue = "") String itemName,
			@RequestParam(defaultValue = "") String comment,
			Model model) {

		List<String> errer = new ArrayList<>();

		if (addDate == null) {
			errer.add("日付を入力してください");
		}
		if (price == null) {
			errer.add("金額を入力してください");
		}
		if (itemName.equals("")) {
			errer.add("名前を入力してください");
		}

		if (errer.isEmpty()) {
			User user = userRepository.findById(accountLogin.getId()).get();
			Genre genre = genreRepository.findById(genreId).get();
			Item item = new Item(itemName, user, genre, price, addDate, comment);
			itemRepository.save(item);
			return "redirect:/items";

		} else {
			model.addAttribute("errers", errer);
			return "item/addItem";
		}
	}

	@GetMapping("/items/{id}/edit")
	public String edit(@PathVariable Integer id,
			Model model) {
		genreList(model);
		Item item = itemRepository.findById(id).get();
		model.addAttribute("item", item);
		return "item/editItem";
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

	@PostMapping("/items/{id}/delete")
	public String delete(@PathVariable Integer id,
			Model model) {
		itemRepository.deleteById(id);
		return "redirect:/items";
	}

}
