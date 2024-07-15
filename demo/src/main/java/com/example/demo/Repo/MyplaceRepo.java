package com.example.demo.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Myplace;

public interface MyplaceRepo extends JpaRepository<Myplace, Integer> {
	List<Myplace> findByUid(String uid);
	Long countByUidAndPlaceId(String uid, String placeid);
	Myplace findByUidAndPlaceId(String uid, String placeid);
}
