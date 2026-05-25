package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "budgets")
public class Budget {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	@ManyToOne
	@JoinColumn(name = "genre_id")
	private Genre genre;
	private Integer year;
	private Integer month;
	private Integer amount;
	@Transient
	private Integer achievement;

	public Budget() {
	}

	public Budget(User user,
			Genre genre,
			Integer year,
			Integer month,
			Integer amount) {
		this.user = user;
		this.genre = genre;
		this.year = year;
		this.month = month;
		this.amount = amount;
	}

	public Integer getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public Genre getGenre() {
		return genre;
	}

	public Integer getYear() {
		return year;
	}

	public Integer getMonth() {
		return month;
	}

	public Integer getAmount() {
		return amount;
	}

	public Integer getAchievement() {
		return achievement;
	}

	public void setAchievement(Integer achievement) {
		this.achievement = achievement;
	}

	public void update(User user,
			Genre genre,
			Integer year,
			Integer month,
			Integer amount) {
		this.user = user;
		this.genre = genre;
		this.year = year;
		this.month = month;
		this.amount = amount;
	}

}
