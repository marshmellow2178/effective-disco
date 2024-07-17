package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Repo.ReviewRepo;
import com.example.demo.entity.Company;
import com.example.demo.entity.Review;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cmp/review")
public class CmpReviewCtrl {
	private final ReviewRepo reviewRepo;
	
	@GetMapping("/list")
	public String getReviewList(
			@RequestParam(value = "page", defaultValue = "1") int page,
			HttpSession session,
			Model model) {
		Company cmp = (Company)session.getAttribute("cmpInfo");
		if(cmp==null) {
			return "redirect:/cmp/login";
		}
		if(page <= 0) { page = 1; }
		Pageable pageable = PageRequest.of(page-1, 10, Direction.DESC, "createDate");
		Page<Review> reviewPage = reviewRepo.findByPlaceId(cmp.getPlaceId(), pageable);
		model.addAttribute("reviewPage", reviewPage);
		return "cmp_review_list";
	}
}
