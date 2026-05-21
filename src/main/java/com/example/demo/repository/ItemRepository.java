package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {

	// ユーザーのアイテムを日時順で一覧表示
	List<Item> findByUser_IdOrderByAddDate(Integer id);

	// ユーザーの月ごとの取得
	List<Item> findByUser_IdAndAddDateBetweenOrderByAddDate(Integer userId, LocalDate startDate, LocalDate endDate);

	// ユーザーのアイテムを日時順でジャンル絞り込み一覧表示
	List<Item> findByUser_IdAndGenre_IdOrderByAddDate(Integer userId, Integer genreId);

	List<Item> findByUser_IdAndGenre_IdAndAddDateBetweenOrderByAddDate(Integer userId, Integer genreId,
			LocalDate startDate, LocalDate endDate);
}
