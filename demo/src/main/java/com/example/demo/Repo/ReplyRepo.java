package com.example.demo.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Reply;

public interface ReplyRepo extends JpaRepository<Reply, Integer> {
	Page<Reply> findByNoticeSeq(Pageable pageable, int noticeSeq);
	Reply findBySeq(int replySeq);
}
