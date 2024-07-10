package com.example.demo.entity;

import java.time.LocalDateTime;
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
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String content;
	
	private int score;
	
	@Column(name = "rec_count")
	private int recCount;
	
	@Column(name = "create_date")
	private LocalDateTime createDate;
	
	@Column(name = "modify_date")
	private LocalDateTime modifyDate;
	
	private String uid;
	
	@Column(name = "place_id")
	private String placeId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uid", insertable = false, updatable = false)
	private Userinfo user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place_id", insertable = false, updatable = false)
	private Place place;
	
	public Review update(String content, int score) {
		this.content = content;
		this.score = score;
		this.modifyDate = LocalDateTime.now();
		return this;
	}
	
	public void recommend() {
		this.recCount++;
	}
	
	public void recommendCancel() {
		this.recCount--;
	}
	
	public Review() {
		
	}
	
	public Review(String content, int score, Userinfo user, Place place) {
		this.content = content;
		this.score = score;
		this.createDate = LocalDateTime.now();
		this.uid = user.getId();
		this.placeId = place.getId();
	}
}
