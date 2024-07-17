package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Purchase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "purchase_id")
	private int id;
	
	@Column(name = "ORDER_ID")
	private int orderId;
	
	@Column(name = "PURCHASE_NAME")
	private String name;
	
	@Column(name = "PURCHASE_COUNT")
	private int count;
	
	@Column(name = "PURCHASE_PRICE")
	private int price;
	
	@Column(name = "PURCHASE_IMG")
	private String img;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDER_ID", insertable = false, updatable = false)
	private OrderInfo order;
	
	public void setUser(OrderInfo order) {
		this.order = order;
		this.orderId = order.getId();
	}
}
