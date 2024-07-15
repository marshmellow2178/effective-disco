package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Repo.MyplaceRepo;
import com.example.demo.Repo.UserRepo;
import com.example.demo.entity.Myplace;
import com.example.demo.entity.Userinfo;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeCtrl {
	
	private final UserRepo userRepo;
	private final MyplaceRepo mpRepo;
	
	@GetMapping("/")
	public String index(
			HttpSession session,
			Model model) {
		Userinfo user = (Userinfo)session.getAttribute("userInfo");
		if(user!=null) {
			List<Myplace> myPlaceList = mpRepo.findByUid(user.getId());
			model.addAttribute("myPlaceList", myPlaceList);
		}
		return "index";
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
		Userinfo user = userRepo.findByIdAndPwd(userId, pwd);
		if(user==null) {
			return "login_form";
		}
		session.setAttribute("userInfo", user);
		return "redirect:/";
	}
	
	@GetMapping("/mypage")
	public String mypage(
			HttpSession session) {
		Userinfo user = (Userinfo)session.getAttribute("userInfo");
		if(user==null) {
			return "login_form";
		}
		return "mypage";
	}
}
