package com.example.demo.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Repo.BrandRepo;
import com.example.demo.entity.Brand;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/brand")
public class BrandCtrl {

	private final BrandRepo brandRepo;
	
	@GetMapping("/list")
	@ResponseBody
	public List<Brand> list(
			@RequestParam(value = "ctgr") String ctgr) {
		return brandRepo.findByCtgr(ctgr.toUpperCase());
	}
}
