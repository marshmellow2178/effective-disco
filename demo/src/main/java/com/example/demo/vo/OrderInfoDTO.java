package com.example.demo.vo;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.OrderInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderInfoDTO {
	private LocalDateTime orderDate;
	private String cmpName;
	private List<OrderDetail> detailList;
	
	public void setOrder(OrderInfo order) {
		this.orderDate = order.getDate();
		this.cmpName = order.getCmpName();
	}
}
