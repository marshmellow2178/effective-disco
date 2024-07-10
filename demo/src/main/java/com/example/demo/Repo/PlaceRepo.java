package com.example.demo.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Place;

public interface PlaceRepo extends JpaRepository<Place, String> {
	Optional<Place> findById(String id);
}
