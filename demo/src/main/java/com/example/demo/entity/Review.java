package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	
	@OneToMany(mappedBy = "review")
	private List<Recommend> recommendList;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uid", insertable = false, updatable = false)
	private Userinfo user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place_id", insertable = false, updatable = false)
	private Place place;
	
	public Review recommend(Recommend r) {
		this.recommendList.add(r);
		this.recCount = recommendList.size();
		return this;
	}
	
	public Review recommendCancel(Recommend r) {
		this.recommendList.remove(r);
		this.recCount = recommendList.size();
		return this;
	}
	
	public List<String> getRecommendIdList(){
		List<String> recommendIdList = new ArrayList<String>();
		for(Recommend r: this.recommendList) {
			recommendIdList.add(r.getUid());
		}
		return recommendIdList;
	}
}
