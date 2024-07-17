package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

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
	
	@GetMapping("/load")
	@ResponseBody
	public Place load(
			@RequestParam(value = "id") String id) {
		return placeRepo.findById(id).get();
	}

	@GetMapping("/route")
	public String place(
			@RequestParam(value = "id") String placeid,
			@RequestParam(value = "sd", defaultValue = "0") String startId,
			Model model,
			HttpSession session) {
		Optional<Place> start = placeRepo.findById(startId);
		if(start.isPresent()) {
			model.addAttribute("start", start.get());
		}
		Place place = placeRepo.findById(placeid).get();
		
		model.addAttribute("place", place);
		return "place_route";
	}
	
	@ResponseBody
	@GetMapping("/check")
	public int check(
			@RequestParam(value="id")String id,
			HttpSession session) {
		Userinfo user = (Userinfo)session.getAttribute("userInfo");
		Optional<Place> place = placeRepo.findById(id);
		int check = 0;
		if(place.isPresent()) {
			check++;
			if(user!=null) {
				check += mpRepo.countByUidAndPlaceId(user.getId(), place.get().getId());
			}
		}
		return check;
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
	@GetMapping("/favorite")
	public int favorite(
			@RequestParam(value = "id") String placeId,
			HttpSession session) {
		Userinfo user = (Userinfo) session.getAttribute("userInfo");
		if(user==null) {
			return -1;
		}
		Myplace myplace = mpRepo.findByUidAndPlaceId(user.getId(), placeId);
		if(myplace!=null) {
			mpRepo.delete(myplace);
			return 0;
		}else {
			myplace = new Myplace();
			mpRepo.save(myplace.setMyplace(user, placeRepo.findById(placeId).get()));
			return 1;
		}
	}
	
	@GetMapping("/favorite/mypage")
	public String mypage(
			HttpSession session,
			Model model
			) {
		Userinfo user = (Userinfo) session.getAttribute("userInfo");
		if(user==null) {
			return "redirect:/login";
		}
		List<Myplace> myPlacePage = mpRepo.findByUid(user.getId());
		model.addAttribute("myPlacePage", myPlacePage);
		return "mypage/place";
	}
}
