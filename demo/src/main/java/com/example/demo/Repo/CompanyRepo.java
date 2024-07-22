package com.example.demo.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Company;

public interface CompanyRepo extends JpaRepository<Company, String> {
	Company findByCmpIdAndPwd(String cmpId, String pwd);
	Company findByCmpId(String cmpId);
	Company findByCmpName(String cmpName);
}
