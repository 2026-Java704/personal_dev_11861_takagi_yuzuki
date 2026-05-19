package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {

	List<Item> findByUser_IdOrderByAddDate(Integer id);

	// 特定の年と月のデータを取得する
	//	@Query("SELECT d FROM items d WHERE d.addDate >= :startDate AND d.addDate <= :endDate")
	//	List<Item> findByMonth(
	//			@Param("startDate") LocalDate startDate,
	//			@Param("endDate") LocalDate endDate);

	List<Item> findByUser_IdAndGenre_IdOrderByAddDate(Integer userId, Integer genreId);

}
