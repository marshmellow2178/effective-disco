package com.example.demo.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Review;

public interface ReviewRepo extends JpaRepository<Review, Integer> {
	Review findById(int id);
	
	List<Review> findByCmpId(String cmpId);
	
	Page<Review> findByCmpId(String cmpId, Pageable pageable); 
	
	Page<Review> findByUid(String uid, Pageable pageable); //회원의 모든 리뷰
	
	Review findByUidAndCmpId(String uid, String cmpId); //특정한 장소의 내 리뷰 찾기
}
