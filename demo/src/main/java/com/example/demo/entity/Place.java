package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Place {
	@Id
	private String id;
	
	private String placename;
	
	private String addr;
	
	private String ctgr;
	
	private int brand;
	
	private String latitude;
	
	private String longitude;
}
