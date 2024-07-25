package com.example.demo.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Review;

public interface ReviewRepo extends JpaRepository<Review, Integer> {
	Review findById(int id);
	
	int countByCmpName(String cmpName);
	
	Page<Review> findByCmpName(String cmpName, Pageable pageable); 
	
	List<Review> findByCmpName(String cmpName);
	
	Page<Review> findByUid(String uid, Pageable pageable); //회원의 모든 리뷰
	
	Review findByUidAndCmpName(String uid, String cmpName); //특정한 장소의 내 리뷰 찾기
	
	int countByCmpNameAndScore(String cmpName, int score); //개수세기
}
