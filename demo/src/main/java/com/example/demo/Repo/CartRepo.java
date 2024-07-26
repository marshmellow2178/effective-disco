package com.example.demo.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Cart;

public interface CartRepo extends JpaRepository<Cart, Integer> {
	Cart findByUserIdAndProductId(String userId, int productId);
	Page<Cart> findByUserId(String userId, Pageable pageable);
	List<Cart> findByUserId(String userId);
}
