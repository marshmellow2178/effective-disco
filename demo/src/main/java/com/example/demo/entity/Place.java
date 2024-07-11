package com.example.demo.entity;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DynamicUpdate
public class Place {
	@Id
	private String id;
	
	private String placename;
	
	private String addr;
	
	private String ctgr;
	
	private int brand;
	
	private String latitude;
	
	private String longitude;
	
	public void update(String placename, String addr, String ctgr, int brand) {
		this.placename = placename;
		this.addr = addr;
		this.ctgr = ctgr;
		this.brand = brand;
	}
}
