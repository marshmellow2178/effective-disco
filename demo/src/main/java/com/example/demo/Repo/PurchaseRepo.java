package com.example.demo.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Purchase;

public interface PurchaseRepo extends JpaRepository<Purchase, Integer> {
	List<Purchase> findByOrderId(int orderId);
}
