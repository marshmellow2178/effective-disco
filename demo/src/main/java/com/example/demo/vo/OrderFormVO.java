package com.example.demo.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFormVO {
	private int[] pid;
	private int[] count;
	private String cmp_name;
	private int order_type;
	private int total_price;
}
