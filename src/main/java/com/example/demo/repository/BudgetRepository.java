package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {

	List<Budget> findByUser_IdAndYearAndMonthOrderByYearDescMonthDesc(Integer userId, Integer year, Integer month);

	List<Budget> findByUser_IdAndYearAndMonth(Integer userId, Integer year, Integer month);
}
