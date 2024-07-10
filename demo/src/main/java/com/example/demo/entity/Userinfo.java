package com.example.demo.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Userinfo {
	@Id
	private String id;
	
	private String pwd;
	
	@OneToMany(mappedBy = "user")
	private List<Recommend> recommendList;
}
