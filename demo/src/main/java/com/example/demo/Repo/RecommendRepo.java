package com.example.demo.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Recommend;

public interface RecommendRepo extends JpaRepository<Recommend, Integer> {
	List<Recommend> findByUid(String uid);
	Page<Recommend> findByUid(String uid, Pageable pageable);
	Recommend findByReviewIdAndUid(int reviewId, String uid);
	int countByReviewIdAndUid(int reviewId, String uid);
	List<Recommend> findByReviewId(int reviewId);
	int countByReviewId(int reviewId);
}
