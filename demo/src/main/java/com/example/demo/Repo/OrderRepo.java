package com.example.demo.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.OrderInfo;

public interface OrderRepo extends JpaRepository<OrderInfo, Integer> {
	Page<OrderInfo> findByCmpId(String cmpId, Pageable pageable);
}
