package com.example.demo.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Repo.FavoriteRepo;
import com.example.demo.entity.Favorite;
import com.example.demo.entity.UserInfo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/fav")
public class FavCtrl {

	private final FavoriteRepo favRepo;
	
	@ResponseBody
	@GetMapping("/del")
	public void addFav(
			@RequestParam(value = "id") String cmpId,
			HttpSession session) {
		UserInfo user = (UserInfo) session.getAttribute("userInfo");
		Favorite fav = favRepo.findByUidAndCmpId(user.getId(), cmpId);
		favRepo.delete(fav);
	}
	
	@ResponseBody
	@GetMapping("/add")
	public void delFav(
			@RequestParam(value = "id") String cmpId,
			HttpSession session) {
		UserInfo user = (UserInfo) session.getAttribute("userInfo");
		Favorite fav = new Favorite();
		fav.setUid(user.getId());
		fav.setCmpId(cmpId);
		favRepo.save(fav);
	}
	
	@GetMapping("/list")
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
