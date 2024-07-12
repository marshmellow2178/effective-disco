package com.example.demo.controller;

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
import com.example.demo.entity.Brand;
import com.example.demo.entity.Myplace;
import com.example.demo.entity.Place;
import com.example.demo.entity.Userinfo;
import com.example.demo.vo.PlaceVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceCtrl {

	private final PlaceRepo placeRepo;
	private final MyplaceRepo mpRepo;
	private final BrandRepo bRepo;

	@GetMapping("/detail")
	public String place(
			@RequestParam(value = "id") String placeid,
			Model model,
			HttpSession session) {
		Place place = placeRepo.findById(placeid).get();
		
		model.addAttribute("place", place);
		return "place_detail";
	}
	
	@ResponseBody
	@GetMapping("/check")
	public int check(
			@RequestParam(value="id")String id,
			HttpSession session) {
		Userinfo user = (Userinfo)session.getAttribute("userInfo");
		Optional<Place> place = placeRepo.findById(id);
		if(user!=null) {
			Long check = mpRepo.countByUidAndPlaceId(user.getId(), place.get().getId());
			if(check==1) {
				return 2;
			}
		}
		if(place.isEmpty()) {
			return 0;
		}
		return 1;
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
		Place p = new Place();
		p.setId(pvo.getId());
		p.setCtgr(pvo.getCtgr());
		p.setPlacename(pvo.getName());
		p.setAddr(pvo.getAddr());
		p.setBrand(brand.getId());
		p.setLatitude(pvo.getLatitude());
		p.setLongitude(pvo.getLongitude());
		placeRepo.save(p);
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
