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
	@Column(name = "cmp_id")
	private String id;
	
	@Column(name = "cmp_pwd")
	private String pwd;
	
	@Column(name = "cmp_state")
	private String state;
	
	@Column(name = "cmp_name")
	private String name;
	
	@Column(name = "place_id")
	private String placeId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place_id", insertable = false, updatable = false)
	private Place place;
	
	public void setPlace(Place place) {
		this.place = place;
		this.name = place.getPlacename();
		this.placeId = place.getId();
	}
	
}
