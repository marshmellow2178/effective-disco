package com.example.demo.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Favorite;

public interface FavoriteRepo extends JpaRepository<Favorite, Integer> {
	List<Favorite> findByUid(String uid);
	Long countByUidAndCmpId(String uid, String cmpId);
	Favorite findByUidAndCmpId(String uid, String cmpId);
}
