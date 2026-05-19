package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "items")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "item_name")
	private String itemName;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	@ManyToOne
	@JoinColumn(name = "genre_id")
	private Genre genre;
	private Integer price;
	@Column(name = "add_date")
	private LocalDate addDate;
	private String comment;

	public Item() {
	}

	public Item(String itemName,
			User user,
			Genre genre,
			Integer price,
			LocalDate addDate,
			String comment) {
		this.itemName = itemName;
		this.user = user;
		this.genre = genre;
		this.price = price;
		this.addDate = addDate;
		this.comment = comment;
	}

	public Integer getId() {
		return id;
	}

	public String getItemName() {
		return itemName;
	}

	public User getUser() {
		return user;
	}

	public Genre getGenre() {
		return genre;
	}

	public Integer getPrice() {
		return price;
	}

	public LocalDate getAddDate() {
		return addDate;
	}

	public String getComment() {
		return comment;
	}

	public void update(String itemName,
			Genre genre,
			Integer price,
			LocalDate addDate,
			String comment) {
		this.itemName = itemName;
		this.genre = genre;
		this.price = price;
		this.addDate = addDate;
		this.comment = comment;
	}
}
