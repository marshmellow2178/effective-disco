package com.example.demo.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Notice;

public interface NoticeRepo extends JpaRepository<Notice, Integer> {
	Page<Notice> findByCmpName(Pageable pageable, String cmpName);
	Notice findBySeq(int noticeSeq);
}
