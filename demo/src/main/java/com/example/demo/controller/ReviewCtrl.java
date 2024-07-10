package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Repo.PlaceRepo;
import com.example.demo.Repo.RecommendRepo;
import com.example.demo.Repo.ReviewRepo;
import com.example.demo.entity.Place;
import com.example.demo.entity.Recommend;
import com.example.demo.entity.Review;
import com.example.demo.entity.Userinfo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewCtrl {
	
	private final PlaceRepo placeRepo;
	private final ReviewRepo reviewRepo;
	private final RecommendRepo rcRepo;

	@PostMapping("/create")
	public String create(
			@RequestParam(value = "placeid") String placeId,
			@RequestParam(value = "content") String content,
			@RequestParam(value = "score") int score,
			HttpSession session
			) {
		Userinfo user = (Userinfo) session.getAttribute("userInfo");
		if(user==null) {
			return "redirect:/login";
		}
		Place place = placeRepo.findById(placeId).get();
		Review review = new Review( content, score, user, place);
		reviewRepo.save(review);
		place.review(review);
		return "redirect:/place/detail?id="+placeId;
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
			@RequestParam(value = "placeid") String placeId,
			@RequestParam(value = "content") String content,
			@RequestParam(value = "score") Integer score,
			HttpSession session
			) {
		Userinfo user = (Userinfo) session.getAttribute("userInfo");
		if(user==null) {
			return "redirect:/login";
			
		}
		Review review = reviewRepo.findById(id);
		reviewRepo.save(review.update(content, score));
		return "redirect:/place/detail?id="+placeId;
	}
	
	@GetMapping("/delete")
	public String delete(
			@RequestParam(value = "id") int id,
			HttpSession session
			) {
		Userinfo user = (Userinfo)session.getAttribute("userInfo");
		if(user==null) {
			return "redirect:/login";
		}
		Review review = reviewRepo.findById(id);
		String placeId = review.getPlaceId();
		List<Recommend> recommendList = rcRepo.findByReviewId(id);
		for(int i = 0;i<recommendList.size();i++) {
			rcRepo.delete(recommendList.get(i));
		}
		reviewRepo.delete(review);
		return "redirect:/place/detail?id="+placeId;
	}
	
	@GetMapping("/mypage")
	public String myReview(
			HttpSession session,
			Model model,
			@RequestParam(value = "page", defaultValue = "1") int page
			) {
		Userinfo user = (Userinfo)session.getAttribute("userInfo");
		if(user==null) {
			return "login_form";
		}
		Pageable pageable = PageRequest.of(page-1, 10, Direction.DESC, "createdate");
		Page<Review> myReviewPage = reviewRepo.findByUid(user.getId(), pageable);
		model.addAttribute("myReviewPage", myReviewPage);
		return "mypage_review";
	}
	
	@ResponseBody
	@PostMapping("/recommend")
	public void recommend(
			@RequestBody int reviewId,
			HttpSession session
			) {
		Userinfo user = (Userinfo)session.getAttribute("userInfo");
		Recommend rc = rcRepo.findByReviewIdAndUid(reviewId, user.getId());
		Review review = reviewRepo.findById(reviewId);
		if(rc!=null) {
			review.recommendCancel();
			rcRepo.delete(rc);
		}else {
			review.recommend();
			rc = new Recommend(user, review);
			rcRepo.save(rc);
		}
	}
	
	@GetMapping("/recommend/mypage")
	public String mypage(
			@RequestParam(value = "page", defaultValue = "1") int page,
			HttpSession session,
			Model model) {
		Userinfo user = (Userinfo) session.getAttribute("userInfo");
		if(user==null) {
			return "redirect:/login";
		}
		Pageable pageable = PageRequest.of(page, 10, Direction.DESC, "createdate");
		Page<Review> reviewPage = reviewRepo.findByUid(user.getId(), pageable);
		model.addAttribute("reviewPage", reviewPage);
		return "mypage/review";
	}
}
