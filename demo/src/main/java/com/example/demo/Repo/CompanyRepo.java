package com.example.demo.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Company;

public interface CompanyRepo extends JpaRepository<Company, String> {
	Company findByIdAndPwd(String id, String pwd);
}
