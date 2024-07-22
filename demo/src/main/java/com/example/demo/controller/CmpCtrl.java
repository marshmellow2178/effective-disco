package com.example.demo.controller;

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
import com.example.demo.entity.Brand;
import com.example.demo.entity.Company;
import com.example.demo.vo.PlaceVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cmp")
public class CmpCtrl {
	
	private final CompanyRepo cmpRepo;
	private final BrandRepo bRepo;
	
	@GetMapping("/login")
	public String cmpLogin() {
		return "cmp_login_form";
	}
	
	@PostMapping("/login")
	public String cmpLogin(
			@RequestParam(value = "cmp_id") String cmpId,
			@RequestParam(value = "cmp_pwd") String cmpPwd,
			HttpSession session) {
		Company cmp = cmpRepo.findByCmpIdAndPwd(cmpId, cmpPwd);
		if(cmp==null) {
			return "redirect:/cmp/login";
		}
		session.setAttribute("cmpInfo", cmp);
		return "redirect:/cmp";
	}
	
	@GetMapping("/logout")
	public String cmpLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@GetMapping("/mypage")
	public String cmpPage(
			HttpSession session) {
		Company cmpInfo = (Company)session.getAttribute("cmpInfo");
		if(cmpInfo==null) {
			return "redirect:/cmp/login";
		}
		return "cmp_page";
	}
	
	@GetMapping("/open")
	public String open(HttpSession session) {
		Company cmp = (Company)session.getAttribute("cmpInfo");
		cmp.setState("o");
		session.setAttribute("cmpInfo", cmpRepo.save(cmp));
		return "redirect:/cmp";
	}
	
	@GetMapping("/close")
	public String close(HttpSession session) {
		Company cmp = (Company)session.getAttribute("cmpInfo");
		cmp.setState("c");
		session.setAttribute("cmpInfo", cmpRepo.save(cmp));
		return "redirect:/cmp";
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
			cmp.setCmpAddr(pvo.getAddr());
			cmp.setCmpLat(pvo.getLatitude());
			cmp.setCmpLng(pvo.getLongitude());
			cmp.setPwd("1234");
			cmp.setCmpId(pvo.getName());
			cmp.setScore(0.0);
			cmp.setState("c");
			cmpRepo.save(cmp);
		}
	}
	
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
	@GetMapping("/get")
	public Company getCmp(
			@RequestParam(value="name")String cmpName
			) {

		return cmpRepo.findByCmpName(cmpName);
	}
	
	@GetMapping("/route")
	public String findRoute(
			@RequestParam(value="sname")String startName,
			@RequestParam(value="ename")String cmpName,
			Model model) {
		if(!startName.equals("현재 위치")) {
			Company start = cmpRepo.findByCmpName(startName);
			model.addAttribute("start", start);
		}
		Company end = cmpRepo.findByCmpName(cmpName);
		model.addAttribute("end", end);
		return "cmp_route";
	}
}
