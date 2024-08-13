package com.example.demo.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Repo.CompanyRepo;
import com.example.demo.Repo.NoticeRepo;
import com.example.demo.Repo.ReplyRepo;
import com.example.demo.entity.Company;
import com.example.demo.entity.Notice;
import com.example.demo.entity.Reply;
import com.example.demo.entity.UserInfo;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeCtrl {
	private final CompanyRepo cmpRepo;
	private final NoticeRepo noticeRepo;
	private final ReplyRepo replyRepo;
	
	@GetMapping("/list")
	public String getNoticeList(
			@RequestParam(value = "cmp") String cmpName,
			@RequestParam(value = "page", defaultValue = "1") int page,
			Model model) {
		if(page<1) {page=1;}
		Pageable pageable = PageRequest.of(page-1, 10, Direction.DESC, "createDate");
		Page<Notice> noticePage = noticeRepo.findByCmpName(pageable, cmpName);
		model.addAttribute("noticePage", noticePage);
		return "notice_list";
	}
	
	@GetMapping("/list/cmp")
	public String getCmpNoticeList(
			@RequestParam(value = "page", defaultValue = "1") int page,
			HttpSession session,
			Model model) {
		Company cmp = (Company)session.getAttribute("cmpInfo");
		if(cmp==null) {
			return "redirect:/cmp/login";
		}
		if(page<1) {page=1;}
		Pageable pageable = PageRequest.of(page-1, 10, Direction.DESC, "createDate");
		Page<Notice> noticePage = noticeRepo.findByCmpName(pageable, cmp.getCmpName());
		model.addAttribute("noticePage", noticePage);
		return "cmp_notice_list";
	}
	
	@GetMapping("/detail")
	public String getNoticeDetail(
			@RequestParam(value = "id") int noticeSeq,
			@RequestParam(value = "page", defaultValue = "1") int page,
			Model model
			) {
		if(page<1) {page=1;}
		Pageable pageable = PageRequest.of(page-1, 10, Direction.DESC, "createDate");
		Notice notice = noticeRepo.findBySeq(noticeSeq);
		Page<Reply> replyPage = replyRepo.findByNoticeSeq(pageable, noticeSeq);
		model.addAttribute("notice", notice);
		model.addAttribute("replyPage", replyPage);
		return "notice_detail";
	}
	
	@GetMapping("/detail/cmp")
	public String getCmpNoticeDetail(
			@RequestParam(value = "id") int noticeSeq,
			@RequestParam(value = "page", defaultValue = "1") int page,
			Model model
			) {
		if(page<1) {page=1;}
		Pageable pageable = PageRequest.of(page-1, 10, Direction.DESC, "createDate");
		Notice notice = noticeRepo.findBySeq(noticeSeq);
		Page<Reply> replyPage = replyRepo.findByNoticeSeq(pageable, noticeSeq);
		model.addAttribute("notice", notice);
		model.addAttribute("replyPage", replyPage);
		return "cmp_notice_detail";
	}
	
	@PostMapping("/reply")
	public String replyNotice(
			@RequestParam(value = "id") int noticeSeq,
			@RequestParam(value = "content") String content,
			HttpSession session
			) {
		UserInfo user = (UserInfo)session.getAttribute("userInfo");
		
		if(user==null) {
			return "redirect:/login";
		}
		Reply reply = new Reply();
		reply.setContent(content);
		reply.setCreateDate(LocalDateTime.now());
		reply.setNoticeSeq(noticeSeq);
		reply.setUserId(user.getId());
		replyRepo.save(reply);
		return "redirect:/notice/detail?id="+noticeSeq;
	}
	
	@PostMapping("/reply/delete")
	public String deleteReply(
			HttpSession session,
			@RequestParam(value = "id") int replySeq
			) {
		UserInfo user = (UserInfo)session.getAttribute("userInfo");
		if(user==null) {
			return "redirect:/login";
		}
		Reply reply = replyRepo.findBySeq(replySeq);
		int noticeSeq = reply.getNoticeSeq();
		if(user.getId().equals(reply.getUserId())) {
			replyRepo.delete(reply);
		}
		return "redirect:/notice/detail?id="+noticeSeq;
	}
}
