package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Category {
	@Id
	@Column(name = "ctgr_name")
	private String name;
	
	@Column(name = "ctgr_text")
	private String text;
}
