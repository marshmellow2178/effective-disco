package com.example.demo.controller;

import java.time.LocalDateTime;
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
import com.example.demo.vo.RecommendVO;

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
		Review review = new Review();
		review.setContent(content);
		review.setScore(score);
		review.setUser(user);
		review.setPlace(place);
		review.setCreateDate(LocalDateTime.now());
		reviewRepo.save(review);
		return "redirect:/review/list?id="+placeId;
	}
	
	@GetMapping("/list")
	public String getList(
			@RequestParam(value = "id") String placeid,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "sort", defaultValue = "r") String sort,
			Model model,
			HttpSession session) {
		if(page<=0) { page = 1; }
		Pageable pageable = PageRequest.of(page-1, 10, Direction.DESC, "recCount");
		if(sort.equals("d")) {
			pageable = PageRequest.of(page-1, 10, Direction.ASC, "createDate");
		}
		Userinfo user = (Userinfo)session.getAttribute("userInfo");
		Place place = placeRepo.findById(placeid).get();
		List<Review> reviewList = reviewRepo.findByPlaceId(placeid);		
		int[] scores = new int[6];
		double avgScore = 0.0;
		for(int i = 0;i<reviewList.size();i++) {
			Review review = reviewList.get(i);
			int score = review.getScore();
			scores[0] += score;
			scores[score]++; //[총점, 1점인원, 2점인원, 3점인원, 4점인원, 5점인원]
			if(user!=null && review.getRecCount()>0) {
				List<Recommend> rList = rcRepo.findByReviewId(review.getId());
				for(Recommend r: rList) {
					if(r.getUid().equals(user.getId())) {
						review.setRecommend(true);
						break;
					}
				}
			} //리뷰 추천 여부 확인하기
		}
		avgScore = Math.round((double)scores[0]/reviewList.size()*10);
		avgScore /=10;
		
		if(user!=null) {
			Review myReview = reviewRepo.findByUidAndPlaceId(user.getId(), placeid);
			model.addAttribute("myReview", myReview);
		}
		model.addAttribute("place", place);
		model.addAttribute("reviewPage", reviewRepo.findByPlaceId(placeid, pageable));
		model.addAttribute("scoreInfo", scores);
		model.addAttribute("avgScore", avgScore);
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
		review.setContent(content);
		review.setScore(score);
		review.setModifyDate(LocalDateTime.now());
		reviewRepo.save(review);
		return "redirect:/review/list?id="+placeId;
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
		reviewRepo.delete(review);
		return "redirect:/review/list?id="+review.getPlaceId();
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
			@RequestBody RecommendVO revo,
			HttpSession session
			) {
		Userinfo user = (Userinfo)session.getAttribute("userInfo");
		Review review = reviewRepo.findById(revo.getReviewId());
		Recommend rc = rcRepo.findByReviewIdAndUid(revo.getReviewId(), user.getId());
		if(rc!=null) {
			rcRepo.delete(rc);
		}else {
			rc = new Recommend();
			rc.setUser(user);
			rc.setReview(review);
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
		return "mypage_review";
	}
}
