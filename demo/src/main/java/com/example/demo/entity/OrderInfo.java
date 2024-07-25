package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

import com.example.demo.vo.OrderFormVO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DynamicUpdate
public class OrderInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private int id;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "cmp_name")
	private String cmpName;
	
	@Column(name = "order_price")
	private int price;
	
	@Column(name = "order_state")
	private int state;
	
	@Column(name = "ORDER_DATE")
	private LocalDateTime date;
	
	@Column(name = "order_type")
	private int type;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cmp_name", insertable = false, updatable = false)
	private Company cmp;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private UserInfo user;
	
	@OneToMany(mappedBy="order")
	private List<OrderDetail> odList = new ArrayList<>();
	
	public void createOrder(OrderFormVO ofvo) {
		this.cmpName = ofvo.getCmp_name();
		this.date = LocalDateTime.now();
		this.price = ofvo.getTotal_price();
		this.type = ofvo.getOrder_type();
		this.state = 0;
	}
	
	public void updateOrder(int state) {
		this.state = state;
	}
}
