package com.example.demo.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Brand;

public interface BrandRepo extends JpaRepository<Brand, Integer> {
	Brand findById(int id);
	List<Brand> findByCtgr(String ctgr);
	Brand findByName(String name);
}
