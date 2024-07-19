package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Repo.BrandRepo;
import com.example.demo.Repo.CtgrRepo;
import com.example.demo.Repo.FavoriteRepo;
import com.example.demo.Repo.UserRepo;
import com.example.demo.entity.Brand;
import com.example.demo.entity.Category;
import com.example.demo.entity.Favorite;
import com.example.demo.entity.UserInfo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeCtrl {
	
	private final UserRepo userRepo;
	private final FavoriteRepo favRepo;
	private final CtgrRepo ctgrRepo;
	private final BrandRepo bRepo;
	
	@GetMapping("/")
	public String index(
			HttpSession session,
			Model model) {
		UserInfo user = (UserInfo)session.getAttribute("userInfo");
		if(user!=null) {
			List<Favorite> favList = favRepo.findByUid(user.getId());
			model.addAttribute("favList", favList);
		}
		List<Category> ctgrList = ctgrRepo.findAll();
		model.addAttribute("ctgrList", ctgrList);
		return "index";
	}
	
	@GetMapping("/brand/list")
	@ResponseBody
	public List<Brand> list(
			@RequestParam(value = "ctgr") String ctgr) {
		return bRepo.findByCtgr(ctgr.toUpperCase());
	}
	
	@GetMapping("/login")
	public String login() {
		return "login_form";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("userInfo"); 
		return "redirect:/";
	}
	
	@PostMapping("/login")
	public String login(
			HttpSession session,
			@RequestParam(value = "userId") String userId,
			@RequestParam(value = "pwd") String pwd) {
		UserInfo user = userRepo.findByIdAndPwd(userId, pwd);
		if(user==null) {
			return "login_form";
		}
		session.setAttribute("userInfo", user);
		return "redirect:/";
	}
	
	@GetMapping("/mypage")
	public String mypage(
			HttpSession session) {
		UserInfo user = (UserInfo)session.getAttribute("userInfo");
		if(user==null) {
			return "login_form";
		}
		return "mypage";
	}
}
