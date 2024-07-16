package com.example.demo.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Repo.CompanyRepo;
import com.example.demo.Repo.PlaceRepo;
import com.example.demo.entity.Company;
import com.example.demo.entity.Place;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cmp")
public class CmpCtrl {
	
	private final CompanyRepo cmpRepo;
	private final PlaceRepo placeRepo;
	
	@GetMapping("/")
	public String index(HttpSession session,
			Model model,
			@PageableDefault(size = 12, sort = "date") Pageable pageable) {
		Company cmpInfo = (Company)session.getAttribute("cmpInfo");
		if(cmpInfo==null) {
			return "redirect:/cmp/login";
		}
		Place place = placeRepo.findById(cmpInfo.getPlaceId()).get();
		if(cmpInfo.getState().equals("o")) {
			
		}
		session.setAttribute("placeInfo", place);
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
	
	@GetMapping("/state")
	public String open(HttpSession session) {
		Company cmp = (Company)session.getAttribute("cmpInfo");
		if(cmp.getState().equals("o")) {
			cmp.setState("c");
		}else {
			cmp.setState("o");
		}
		cmpRepo.save(cmp);
		session.setAttribute("cmpInfo", cmp);
		return "redirect:/cmp/";
	}

}
