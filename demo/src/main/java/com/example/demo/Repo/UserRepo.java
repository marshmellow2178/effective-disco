package com.example.demo.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.UserInfo;

public interface UserRepo extends JpaRepository<UserInfo, String> {
	UserInfo findByIdAndPwd(String id, String pwd);
}
