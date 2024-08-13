package com.example.demo.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicUpdate;

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
@DynamicUpdate
public class Notice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notice_seq")
	private int seq;
	
	@Column(name = "notice_title")
	private String title;
	
	@Column(name = "notice_content")
	private String content;
	
	@Column(name = "create_date")
	private LocalDateTime createDate;
	
	@Column(name = "modify_date")
	private LocalDateTime modifyDate;
	
	@Column(name = "notice_read")
	private int read;
	
	@Column(name = "notice_like")
	private int like;
	
	@Column(name = "cmp_name")
	private String cmpName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cmp_name", insertable = false, updatable = false)
	private Company cmp;
	
	public void setCmp(Company cmp) {
		this.cmp = cmp;
		this.cmpName = cmp.getCmpName();
	}
}
