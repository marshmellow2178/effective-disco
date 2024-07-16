package com.example.demo.entity;

import java.time.LocalDateTime;

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
	
	@Column(name = "cmp_id")
	private String cmpId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cmp_id", insertable = false, updatable = false)
	private Company company;
	
	public void setCompany(Company cmp) {
		this.company = cmp;
		this.cmpId = cmp.getId();
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private Userinfo user;
	
	public void setUser(Userinfo user) {
		this.user = user;
		this.userId = user.getId();
	}
}
