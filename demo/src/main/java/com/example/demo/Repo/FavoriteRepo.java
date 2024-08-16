package com.example.demo.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Favorite;

public interface FavoriteRepo extends JpaRepository<Favorite, Integer> {
	Page<Favorite> findByUid(String uid, Pageable pageable);
	List<Favorite> findByUid(String uid);
	Favorite findByUidAndCmpSeq(String uid, int cmpSeq);
}
