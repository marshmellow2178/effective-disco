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
public class Reply {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reply_seq")
	private int seq;

	@Column(name = "reply_content")
	private String content;
	
	@Column(name = "create_date")
	private LocalDateTime createDate;
	
	@Column(name = "modify_date")
	private LocalDateTime modifyDate;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "notice_seq")
	private int noticeSeq;
	
	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private UserInfo user;
	
	public void setUser(UserInfo user) {
		this.user = user;
		this.userId = user.getId();
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notice_seq", insertable = false, updatable = false)
	private Notice notice;
	
	public void setNotice(Notice notice) {
		this.notice = notice;
		this.noticeSeq = notice.getSeq();
	}

}
