package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "genres")
public class Genre {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "genre_name")
	private String genreName;
	@Column(name = "is_income")
	private String isIncome;

	public Genre() {
	}

	public Genre(String genreName, String isIncome) {
		this.genreName = genreName;
		this.isIncome = isIncome;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGenreName() {
		return genreName;
	}

	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}

	public String getIsIncome() {
		return isIncome;
	}

	public void setIsIncome(String isIncome) {
		this.isIncome = isIncome;
	}

}
