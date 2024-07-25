package com.example.demo.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.OrderDetail;

public interface OrderDetailRepo extends JpaRepository<OrderDetail, Integer> {
	List<OrderDetail> findByOrderId(int orderId);
}
