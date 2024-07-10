package com.example.demo.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Userinfo;

public interface UserRepo extends JpaRepository<Userinfo, String> {
	Userinfo findByIdAndPwd(String id, String pwd);
}
