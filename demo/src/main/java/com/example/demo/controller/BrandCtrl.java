package com.example.demo.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Repo.BrandRepo;
import com.example.demo.Repo.MybrandRepo;
import com.example.demo.entity.Brand;
import com.example.demo.entity.Mybrand;
import com.example.demo.entity.Userinfo;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/brand")
public class BrandCtrl {

	private final BrandRepo brandRepo;
	private final MybrandRepo mbRepo;
	
	@GetMapping("/list")
	public String list(
			Model model) {
		List<Brand> brandList = brandRepo.findAll();
		model.addAttribute("brandList", brandList);
		return "brand_list";
	}
	
	@ResponseBody
	@PostMapping("/favorite")
	public void favorite(
			HttpSession session,
			@RequestBody int brandId) {
		Userinfo user = (Userinfo)session.getAttribute("Userinfo");
		Mybrand mb = mbRepo.findByBrandIdAndUid(brandId, user.getId());
		if(mb!=null) {
			mbRepo.delete(mb);
		}else {
			mb = new Mybrand(user, brandRepo.findById(brandId));
			mbRepo.save(mb);
		}
	}
	
	@GetMapping("/favorite/mypage")
	public String favoritePage(
			HttpSession session, 
			Model model) {
		Userinfo user = (Userinfo)session.getAttribute("Userinfo");
		if(user==null) {
			return "redirect:/login";
		}
		List<Mybrand> myBrandList = mbRepo.findByUid(user.getId());
		model.addAttribute("myBrandList", myBrandList);
		return "mypage_brand";
	}
}
