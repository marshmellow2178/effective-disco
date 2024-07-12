package com.example.demo.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Review;

public interface ReviewRepo extends JpaRepository<Review, Integer> {
	Review findById(int id);
	
	List<Review> findByPlaceId(String placeId);
	
	Page<Review> findByPlaceId(String placeId, Pageable pageable); 
	
	Page<Review> findByUid(String uid, Pageable pageable); //회원의 모든 리뷰
	
	Review findByUidAndPlaceId(String uid, String placeId); //특정한 장소의 내 리뷰 찾기
}
