package com.example.demo.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Repo.BrandRepo;
import com.example.demo.Repo.MyplaceRepo;
import com.example.demo.Repo.PlaceRepo;
import com.example.demo.Repo.ReviewRepo;
import com.example.demo.entity.Brand;
import com.example.demo.entity.Myplace;
import com.example.demo.entity.Place;
import com.example.demo.entity.Review;
import com.example.demo.entity.Userinfo;
import com.example.demo.vo.PlaceVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceCtrl {
	
	private final ReviewRepo reviewRepo;
	private final PlaceRepo placeRepo;
	private final MyplaceRepo mpRepo;
	private final BrandRepo bRepo;

	@GetMapping("/detail")
	public String place(
			@RequestParam(value = "id") String placeid,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "order", defaultValue = "createDate") String order,
			Model model,
			HttpSession session
			) {
		Userinfo user = (Userinfo) session.getAttribute("userInfo");
		if(user==null) {
			return "redirect:/login";
		}
		Pageable pageable = PageRequest.of(page, 10, Direction.ASC, order);
		Page<Review> reviewPage = reviewRepo.findByPlaceId(placeid, pageable);
		Place place = placeRepo.findById(placeid).get();
		List<Review> reviewList = place.getReviewList();
		int[] scores = new int[6];
		double avgScore = 0.0;
		if(reviewPage.getTotalElements()!=0) {
			for(int i = 0;i<reviewList.size();i++) {
				int score = reviewList.get(i).getScore();
				scores[0] += score;
				scores[score]++;
			} //[총점, 1점인원, 2점인원, 3점인원, 4점인원, 5점인원]
			avgScore = Math.round((double)scores[0]/reviewList.size()*10);
			avgScore /=10;
		}
		Review myReview = reviewRepo.findByUidAndPlaceId(user.getId(), placeid);
		model.addAttribute("myReview", myReview);
		model.addAttribute("reviewPage", reviewPage);
		model.addAttribute("scoreInfo", scores);
		model.addAttribute("avgScore", avgScore);
		model.addAttribute("place", place);
		return "place_detail";
	}
	
	@ResponseBody
	@PostMapping("/save")
	public void save(
			@RequestBody PlaceVO pvo) {
		Brand brand = bRepo.findByName(pvo.getBrand());
		if(brand==null) {
			brand = new Brand();
			brand.setCtgr(pvo.getCtgr());
			brand.setName(pvo.getBrand());
			bRepo.save(brand);
		}
		Optional<Place> pOption = placeRepo.findById(pvo.getId());
		if(pOption.isEmpty()) {
			Place p = new Place();
			p.setId(pvo.getId());
			p.setCtgr(pvo.getCtgr());
			p.setPlacename(pvo.getName());
			p.setAddr(pvo.getAddr());
			p.setBrand(brand.getId());
			placeRepo.save(p);
		}else {
			Place p = pOption.get();
			if(p.getBrand()==0) {
				p.setBrand(brand.getId());
				placeRepo.save(p);
			}
		}
	}
	
	@ResponseBody
	@PostMapping("/favorite")
	public void favorite(
			@RequestBody String placeId,
			HttpSession session) {
		Userinfo user = (Userinfo) session.getAttribute("userInfo");
		Myplace myplace = mpRepo.findByUidAndPlaceId(user.getId(), placeId);
		if(myplace!=null) {
			mpRepo.delete(myplace);
		}else {
			myplace = new Myplace(user, placeRepo.findById(placeId).get());
			mpRepo.save(myplace);
		}
	}
	
	@GetMapping("/favorite/mypage")
	public String mypage(
			@RequestParam(value = "page", defaultValue = "1") int page,
			HttpSession session,
			Model model
			) {
		Userinfo user = (Userinfo) session.getAttribute("userInfo");
		if(user==null) {
			return "redirect:/login";
		}
		Pageable pageable = PageRequest.of(page, 10, Direction.DESC, "createdate");
		Page<Myplace> myPlacePage = mpRepo.findByUid(user.getId(), pageable);
		model.addAttribute("myPlacePage", myPlacePage);
		return "mypage/place";
	}
}
