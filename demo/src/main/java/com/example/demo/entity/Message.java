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
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "msg_id")
	private int id;
	
	@Column(name = "msg_content")
	private String content;
	
	@Column(name = "msg_type")
	private String type;
	
	@Column(name = "msg_img")
	private String img;
	
	@Column(name = "cmp_id")
	private String cmpId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cmp_id", insertable = false, updatable = false)
	private Company company;
	
	public void setCompany(Company cmp) {
		this.company = cmp;
		this.cmpId = cmp.getId();
	}
}
