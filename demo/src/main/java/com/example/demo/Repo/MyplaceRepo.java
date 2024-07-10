package com.example.demo.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Myplace;

public interface MyplaceRepo extends JpaRepository<Myplace, Integer> {
	Page<Myplace> findByUid(String uid, Pageable pageable);
	Myplace findByUidAndPlaceId(String uid, String placeid);
}
