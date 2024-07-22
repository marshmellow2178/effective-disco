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
public class Favorite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="uid")
	private String uid;
	
	@Column(name="cmp_name")
	private String cmpName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cmp_name", insertable = false, updatable = false)
	private Company cmp;
}
