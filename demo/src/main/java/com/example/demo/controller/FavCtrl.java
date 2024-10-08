package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Repo.CompanyRepo;
import com.example.demo.Repo.FavoriteRepo;
import com.example.demo.entity.Company;
import com.example.demo.entity.Favorite;
import com.example.demo.entity.UserInfo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/fav")
public class FavCtrl {

	private final FavoriteRepo favRepo;
	private final CompanyRepo cmpRepo;
	
	@GetMapping("/add")
	public String addFav(
			@RequestParam(value = "id") int cmpSeq,
			HttpSession session) {
		UserInfo user = (UserInfo) session.getAttribute("userInfo");
		if(user==null) {
			return "redirect:/login";
		}
		Favorite fav = favRepo.findByUidAndCmpSeq(user.getId(), cmpSeq);
		if(fav==null) {
			Company cmp = cmpRepo.findByCmpSeq(cmpSeq);
			fav = new Favorite();
			fav.setCmp(cmp);
			fav.setUid(user.getId());
			favRepo.save(fav);
		}
		return "redirect:/fav/list";
	}
	
	@GetMapping("/del")
	public String delFav(
			@RequestParam(value = "id") int favId) {
		Favorite fav = favRepo.findById(favId).get();
		favRepo.delete(fav);
		return "redirect:/fav/list";
	}
	
	@GetMapping("/list")
	public String mypage(
			@RequestParam(value = "page", defaultValue = "1") int page,
			HttpSession session,
			Model model
			) {
		UserInfo user = (UserInfo) session.getAttribute("userInfo");
		if(user==null) {
			return "redirect:/login";
		}
		if(page <= 1) { page = 1; }
		Pageable pageable = PageRequest.of(page-1, 10);
		Page<Favorite> favPage = favRepo.findByUid(user.getId(), pageable);
		model.addAttribute("favPage", favPage);
		return "fav_list";
	}
}
