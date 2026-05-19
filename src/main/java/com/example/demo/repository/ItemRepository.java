package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {

	List<Item> findByUser_IdOrderByAddDate(Integer id);

	List<Item> findByUser_IdAndGenre_IdOrderByAddDate(Integer userId, Integer genreId);

}
