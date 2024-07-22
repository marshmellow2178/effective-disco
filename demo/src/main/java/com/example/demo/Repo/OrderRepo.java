package com.example.demo.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.OrderInfo;

public interface OrderRepo extends JpaRepository<OrderInfo, Integer> {
	Page<OrderInfo> findByCmpNameAndStateNot(String cmpName, int state, Pageable pageable);
	Page<OrderInfo> findByCmpNameAndState(String cmpName, int state, Pageable pageable);
	OrderInfo findById(int orderId);
	Page<OrderInfo> findByUserId(String userId, Pageable pageable);
	
}
