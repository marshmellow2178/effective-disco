package com.example.demo.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Repo.UserRepo;
import com.example.demo.entity.UserInfo;
import com.example.demo.vo.UserVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserCtrl {
	private final UserRepo userRepo;
	
	@GetMapping("/signup")
	public String signUp() {
		return "signup_form";
	}
	
	@PostMapping("/signup")
	public String signUp(
			UserVO uvo
			) {
		UserInfo user = new UserInfo();
		user.setId(uvo.getId());
		user.setPwd(uvo.getPwd());
		user.setEmail(uvo.getEmail()+"@"+uvo.getDomain()+".com");
		user.setPhone(uvo.getPhone());
		user.setLocation(uvo.getLocation());
		user.setJoinDate(LocalDateTime.now());
		userRepo.save(user);
		return "redirect:/login";
	}
	
	@ResponseBody
	@GetMapping("/find")
	public int findById(
			@RequestParam(value = "id") String id
			) {
		return userRepo.countById(id);
	}
	
}
