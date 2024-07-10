package com.example.demo.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Mybrand;

public interface MybrandRepo extends JpaRepository<Mybrand, Integer> {
	Mybrand findByBrandIdAndUid(int brandId, String uid);
	List<Mybrand> findByUid(String uid);
}
