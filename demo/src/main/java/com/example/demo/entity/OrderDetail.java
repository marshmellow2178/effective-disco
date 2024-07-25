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
public class OrderDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "od_id")
	private int id;
	
	@Column(name = "order_id")
	private int orderId;
	
	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "product_price")
	private int productPrice;
	
	@Column(name = "product_count")
	private int productCount;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", insertable = false, updatable = false)
	private OrderInfo order;
	
	public void setOrder(OrderInfo order) {
		this.order = order;
		this.orderId = order.getId();
	}
	
	public void setProduct(Product p) {
		this.productName = p.getName();
		this.productPrice = p.getPrice();
	}
}
