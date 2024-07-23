package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	private double score;
	
	@Column(name = "cmp_id")
	private String cmpId;
	
	@Column(name = "cmp_review")
	private int cmpReviewCnt;
	
	@Column(name = "cmp_addr")
	private String cmpAddr;
	
	@Column(name = "cmp_lat")
	private String cmpLat;
	
	@Column(name = "cmp_lng")
	private String cmpLng;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ctgr_name", insertable = false, updatable = false)
	private Category ctgr;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "brand_name", insertable = false, updatable = false)
	private Brand brand;
}
