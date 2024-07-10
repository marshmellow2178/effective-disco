package com.example.demo.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Recommend;

public interface RecommendRepo extends JpaRepository<Recommend, Integer> {
	List<Recommend> findByUid(String uid);
	Recommend findByReviewIdAndUid(int reviewId, String uid);
	List<Recommend> findByReviewId(int reviewId);
}
