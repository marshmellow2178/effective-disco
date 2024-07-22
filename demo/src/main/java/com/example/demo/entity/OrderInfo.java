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
public class OrderInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private int id;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "order_price")
	private int price;
	
	@Column(name = "order_state")
	private int state;
	
	@Column(name = "ORDER_DATE")
	private LocalDateTime date;
	
	@Column(name = "cmp_name")
	private String cmpName;
	
	@Column(name = "product_id")
	private int productId;
	
	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "product_count")
	private int productCount;
	
	@Column(name = "product_price")
	private int productPrice;
}
