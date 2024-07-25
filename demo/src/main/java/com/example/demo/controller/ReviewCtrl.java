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
import com.example.demo.Repo.RecommendRepo;
import com.example.demo.Repo.ReviewRepo;
import com.example.demo.entity.Company;
import com.example.demo.entity.Recommend;
import com.example.demo.entity.Review;
import com.example.demo.entity.UserInfo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewCtrl {
	
	private final CompanyRepo cmpRepo;
	private final ReviewRepo reviewRepo;
	private final RecommendRepo rcRepo;

	@PostMapping("/create")
	public String create(
			@RequestParam(value = "cmpId") String cmpId,
			@RequestParam(value = "content") String content,
			@RequestParam(value = "score") int score,
			HttpSession session
			) {
		UserInfo user = (UserInfo) session.getAttribute("userInfo");
		if(user==null) {
			return "redirect:/login";
		}
		Company cmp = cmpRepo.findByCmpName(cmpId);
		Review review = new Review();
		review.setContent(content);
		review.setScore(score);
		review.setUid(user.getId());
		review.setCmpName(cmp.getCmpName());
		review.setCreateDate(LocalDateTime.now());
		reviewRepo.save(review);
		cmp.setCmpReviewCnt(reviewRepo.countByCmpName(cmpId));
		int tempScore = 0;
		for(int i = 1;i<=5;i++) {
			tempScore += (reviewRepo.countByCmpNameAndScore(cmpId, i)*i);
		}
		double dscore = (double)tempScore;
		dscore /= cmp.getCmpReviewCnt();
		dscore = (Math.round(dscore*100));
		cmp.setScore(dscore/100);
		cmpRepo.save(cmp);
		return "redirect:/review/list?id="+cmpId;
	}
	
	@GetMapping("/list")
	public String getList(
			@RequestParam(value = "name", defaultValue = "") String cmpName,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "sort", defaultValue = "r") String sort,
			Model model,
			HttpSession session) {
		if(cmpName.equals("")) {
			cmpName = session.getAttribute("cmp").toString();
		}
		if(page<=0) { page = 1; }
		Pageable pageable = PageRequest.of(page-1, 10, Direction.DESC, "recCount");
		if(sort.equals("d")) {
			pageable = PageRequest.of(page-1, 10, Direction.ASC, "createDate");
		}
		UserInfo user = (UserInfo)session.getAttribute("userInfo");
		Company cmp = cmpRepo.findById(cmpName).get();	
		
		if(user!=null) {
			Review myReview = reviewRepo.findByUidAndCmpName(user.getId(), cmpName);
			model.addAttribute("myReview", myReview);
		}
		int[] scoreArr = new int[5];
		for(int i = 0;i<5;i++) {
			scoreArr[i] = reviewRepo.countByCmpNameAndScore(cmpName, i+1);
		}
		model.addAttribute("cmp", cmp);
		model.addAttribute("scoreArr", scoreArr);
		model.addAttribute("reviewPage", reviewRepo.findByCmpName(cmpName, pageable));
		return "review_list";
	}
	
	@GetMapping("/cmp/list")
	public String getCmpReviewList(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "sort", defaultValue = "r") String sort,
			Model model,
			HttpSession session) {
		Company cmp = (Company) session.getAttribute("cmpInfo");
		if(cmp==null) {
			return "redirect:/cmp/login";
		}
		if(page<=0) { page = 1; }
		Pageable pageable = PageRequest.of(page-1, 10, Direction.DESC, "recCount");
		if(sort.equals("d")) {
			pageable = PageRequest.of(page-1, 10, Direction.ASC, "createDate");
		}
		int[] scoreArr = new int[5];
		for(int i = 0;i<5;i++) {
			scoreArr[i] = reviewRepo.countByCmpNameAndScore(cmp.getCmpName(), i+1);
		}
		model.addAttribute("cmp", cmp);
		model.addAttribute("scoreArr", scoreArr);
		model.addAttribute("reviewPage", reviewRepo.findByCmpName(cmp.getCmpName(), pageable));
		return "review_list";
	}
	
	@GetMapping("/modify")
	public String modify(
			@RequestParam(value = "id") Integer id,
			Model model
			) {
		Review review = reviewRepo.findById((int)id);
		model.addAttribute("myReview", review);
		return "review_form";
	}
	
	@PostMapping("/modify")
	public String modify(
			@RequestParam(value = "id") int id,
			@RequestParam(value = "cmp") String cmp,
			@RequestParam(value = "content") String content,
			@RequestParam(value = "score") Integer score,
			HttpSession session
			) {
		UserInfo user = (UserInfo) session.getAttribute("userInfo");
		if(user==null) {
			return "redirect:/login";
			
		}
		Review review = reviewRepo.findById(id);
		review.setContent(content);
		review.setScore(score);
		review.setModifyDate(LocalDateTime.now());
		reviewRepo.save(review);
		return "redirect:/review/list?name=";
	}
	
	@GetMapping("/delete")
	public String delete(
			@RequestParam(value = "id") int id,
			HttpSession session
			) {
		UserInfo user = (UserInfo)session.getAttribute("userInfo");
		if(user==null) {
			return "redirect:/login";
		}
		Review review = reviewRepo.findById(id);
		reviewRepo.delete(review);
		return "redirect:/review/list?id="+review.getCmpName();
	}
	
	@GetMapping("/mypage")
	public String myReview(
			HttpSession session,
			Model model,
			@RequestParam(value = "page", defaultValue = "1") int page
			) {
		UserInfo user = (UserInfo)session.getAttribute("userInfo");
		if(user==null) {
			return "login_form";
		}
		Pageable pageable = PageRequest.of(page-1, 10, Direction.DESC, "createdate");
		Page<Review> myReviewPage = reviewRepo.findByUid(user.getId(), pageable);
		model.addAttribute("myReviewPage", myReviewPage);
		return "mypage_review";
	}
	
	@GetMapping("/recommend")
	public String recommend(
			@RequestParam(value = "id")int reviewId,
			HttpSession session
			) {
		UserInfo user = (UserInfo)session.getAttribute("userInfo");
		if(user==null) {
			return "redirect:/login";
		}
		Review review = reviewRepo.findById(reviewId);
		Recommend rc = rcRepo.findByReviewIdAndUid(reviewId, user.getId());
		if(rc!=null) {
			rcRepo.delete(rc);
		}else {
			rc = new Recommend();
			rc.setUid(user.getId());
			rc.setReviewId(reviewId);
			rcRepo.save(rc);
		}
		review.setRecCount(rcRepo.countByReviewId(reviewId));
		reviewRepo.save(review);
		return "redirect:/review/list?name=";
	}
	
	@GetMapping("/recommend/list")
	public String mypage(
			@RequestParam(value = "page", defaultValue = "1") int page,
			HttpSession session,
			Model model) {
		UserInfo user = (UserInfo) session.getAttribute("userInfo");
		if(user==null) {
			return "redirect:/login";
		}
		Pageable pageable = PageRequest.of(page, 10, Direction.DESC, "createdate");
		Page<Review> reviewPage = reviewRepo.findByUid(user.getId(), pageable);
		model.addAttribute("reviewPage", reviewPage);
		return "mypage_review";
	}
	
	
}
