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
import com.example.demo.Repo.CompanyRepo;
import com.example.demo.Repo.FavoriteRepo;
import com.example.demo.entity.Brand;
import com.example.demo.entity.Company;
import com.example.demo.entity.Favorite;
import com.example.demo.entity.UserInfo;
import com.example.demo.vo.PlaceVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreCtrl {

	private final CompanyRepo cmpRepo;
	private final FavoriteRepo favRepo;
	private final BrandRepo bRepo;
	
	@ResponseBody
	@GetMapping("/check")
	public int check(
			@RequestParam(value="name")String cmpName
			) {
		Optional<Company> option = cmpRepo.findById(cmpName);
		if(option.isEmpty()) {
			return 0;
		}
		return 1;
	}
	
	@ResponseBody
	@PostMapping("/save")
	public void save(
			@RequestBody PlaceVO pvo) {
		Optional<Brand> option = bRepo.findById(pvo.getBrand());
		if(option.isEmpty()) {
			Brand brand = new Brand();
			brand.setCtgr(pvo.getCtgr());
			brand.setName(pvo.getBrand());
			bRepo.save(brand);
		}
		Optional<Company> cOption = cmpRepo.findById(pvo.getName());
		if(cOption.isEmpty()) {
			Company cmp = new Company();
			cmp.setCmpName(pvo.getName());
			cmp.setBrandName(pvo.getBrand());
			cmp.setCtgrName(pvo.getCtgr());
			cmp.setCmpId(pvo.getName());
			cmpRepo.save(cmp);
		}
	}
	
	@ResponseBody
	@GetMapping("/fav/del")
	public void addFav(
			@RequestParam(value = "id") String cmpId,
			HttpSession session) {
		UserInfo user = (UserInfo) session.getAttribute("userInfo");
		Favorite fav = favRepo.findByUidAndCmpId(user.getId(), cmpId);
		favRepo.delete(fav);
	}
	
	@ResponseBody
	@GetMapping("/fav/add")
	public void delFav(
			@RequestParam(value = "id") String cmpId,
			HttpSession session) {
		UserInfo user = (UserInfo) session.getAttribute("userInfo");
		Favorite fav = new Favorite();
		fav.setUid(user.getId());
		fav.setCmpId(cmpId);
		favRepo.save(fav);
	}
	
	@GetMapping("/fav/list")
	public String mypage(
			HttpSession session,
			Model model
			) {
		UserInfo user = (UserInfo) session.getAttribute("userInfo");
		if(user==null) {
			return "redirect:/login";
		}
		List<Favorite> favPage = favRepo.findByUid(user.getId());
		model.addAttribute("objPage", favPage);
		return "fav_list";
	}
}
