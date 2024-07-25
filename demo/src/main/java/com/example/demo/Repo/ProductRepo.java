package com.example.demo.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {
	Page<Product> findByCmpName(String cmpName, Pageable pageable);
	List<Product> findByCmpName(String cmpName);
	Product findByCmpNameAndName(String cmpName, String name);
}
