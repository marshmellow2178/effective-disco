package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Company {
	@Id
	@Column(name = "cmp_name")
	private String cmpName;
	
	@Column(name = "cmp_pwd")
	private String pwd;
	
	@Column(name = "cmp_state")
	private String state;
	
	@Column(name = "brand_name")
	private String brandName;
	
	@Column(name = "ctgr_name")
	private String ctgrName;
	
	@Column(name = "cmp_score")
	private String score;
	
	@Column(name = "cmp_id")
	private String cmpId;
}
