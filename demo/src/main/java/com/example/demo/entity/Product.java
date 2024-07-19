package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private int id;
	
	@Column(name = "product_name")
	private String name;
	
	@Column(name = "product_price")
	private int price;
	
	@Column(name = "product_img")
	private String img;
	
	@Column(name = "product_count")
	private int count;
	
	@Column(name = "cmp_name")
	private String cmpName;
	
	@Column(name = "create_date")
	private LocalDateTime createDate;
}
