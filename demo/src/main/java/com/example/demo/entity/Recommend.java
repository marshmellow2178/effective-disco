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
public class Recommend {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String uid;
	
	@Column(name = "review_id")
	private int reviewId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uid", insertable = false, updatable = false)
	private Userinfo user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id", insertable = false, updatable = false)
	private Review review;
	
	public void setUser(Userinfo user) {
		this.user = user;
		this.uid = user.getId();
	}
	
	public void setReview(Review review) {
		this.review = review;
		this.reviewId = review.getId();
	}
}
