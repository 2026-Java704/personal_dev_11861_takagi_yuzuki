package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "genres")
public class Genre {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "genre_name")
	private String genreName;
	@Column(name = "is_income")
	private boolean isIncome;

	@Transient
	private Integer total;
	@Transient
	private Integer percent;

	public Genre() {
	}

	public Genre(String genreName, boolean isIncome) {
		this.genreName = genreName;
		this.isIncome = isIncome;
	}

	public Integer getId() {
		return id;
	}

	public String getGenreName() {
		return genreName;
	}

	public boolean getIsIncome() {
		return isIncome;
	}

	public void update(String genreName, boolean isIncome) {
		this.genreName = genreName;
		this.isIncome = isIncome;
	}

	public Integer getTotal() {
		return total;
	}

	public Integer getPercent() {
		return percent;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public void setPercent(Integer percent) {
		this.percent = percent;
	}

}
