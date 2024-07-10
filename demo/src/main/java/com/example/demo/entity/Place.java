package com.example.demo.entity;

import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	
	@OneToMany(mappedBy = "place")
	private List<Review> reviewList;
	
	public void review(Review review) {
		this.reviewList.add(review);
	}
	
	public int[] getScores() {
		int[] scores = new int[6];
		for(int i = 0;i<this.reviewList.size();i++) {
			int score = reviewList.get(i).getScore();
			scores[0] += score;
			scores[score]++;
		}
		return scores;
	} //[총점, 1점인원, 2점인원, 3점인원, 4점인원, 5점인원]

	public Place update(String placename, String addr, String ctgr, int brand) {
		this.placename = placename;
		this.addr = addr;
		this.ctgr = ctgr;
		this.brand = brand;
		return this;
	}
}
