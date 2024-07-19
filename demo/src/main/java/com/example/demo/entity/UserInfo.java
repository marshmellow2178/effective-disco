package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserInfo {
	@Id
	@Column(name = "user_id")
	private String id;
	@Column(name = "user_pwd")
	private String pwd;
}
