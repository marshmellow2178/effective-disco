package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Repo.CompanyRepo;
import com.example.demo.entity.Company;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cmp")
public class CmpCtrl {
	
	private final CompanyRepo cmpRepo;
	
	@GetMapping("/")
	public String index(HttpSession session) {
		Company cmpInfo = (Company)session.getAttribute("cmpInfo");
		if(cmpInfo==null) {
			return "redirect:/cmp/login";
		}
		return "cmp_index";
	}
	
	@GetMapping("/login")
	public String login() {
		return "cmp_login_form";
	}
	
	@PostMapping("/login")
	public String login(
			@RequestParam(value = "cmp_id") String cmpId,
			@RequestParam(value = "cmp_pwd") String cmpPwd,
			HttpSession session) {
		Company cmp = cmpRepo.findByIdAndPwd(cmpId, cmpPwd);
		if(cmp==null) {
			return "redirect:/cmp/login";
		}
		session.setAttribute("cmpInfo", cmp);
		return "redirect:/cmp/";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/cmp/login";
	}
	
	@GetMapping("/open")
	public String open(HttpSession session) {
		Company cmp = (Company)session.getAttribute("cmpInfo");
		cmp.setState("o");
		session.setAttribute("cmpInfo", cmpRepo.save(cmp));
		return "redirect:/cmp/";
	}
	
	@GetMapping("/close")
	public String close(HttpSession session) {
		Company cmp = (Company)session.getAttribute("cmpInfo");
		cmp.setState("c");
		session.setAttribute("cmpInfo", cmpRepo.save(cmp));
		return "redirect:/cmp/";
	}

}
