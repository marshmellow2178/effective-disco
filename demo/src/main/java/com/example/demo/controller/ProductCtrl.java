package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Repo.ProductRepo;
import com.example.demo.entity.Company;
import com.example.demo.entity.Product;
import com.example.demo.vo.ProductDTO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductCtrl {
	
	private final ProductRepo pdRepo;
	
	@GetMapping("/list")
	public String productList(
			Model model,
			@RequestParam(value = "cmp") String cmpName,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		Pageable pageable = PageRequest.of(page-1, 9, Direction.DESC, "createDate");
		Page<Product> products =pdRepo.findByCmpName(cmpName, pageable);
		model.addAttribute("products", products);
		return "product_list";
	}
	
	@GetMapping("/cmp/create")
	public String create(
			HttpSession session
			) {
		Company cmp = (Company)session.getAttribute("cmpInfo");
		if(cmp==null) {
			return "redirect:/cmp/login";
		}
		return "product_form";
	}
	
	@PostMapping("/cmp/create")
	public String create(
			HttpSession session,
			ProductDTO pd) {
		Company cmp = (Company)session.getAttribute("cmpInfo");
		if(cmp==null) {
			return "redirect:/cmp/login";
		}
		Product p = new Product();
		p.setCmpName(cmp.getCmpName());
		p.setName(pd.getName());
		p.setImg(pd.getImg());
		p.setPrice(pd.getPrice());
		p.setCount(pd.getCount());
		p.setCreateDate(LocalDateTime.now());
		pdRepo.save(p);
		return "redirect:/product/cmp/list";
	}
	
	@GetMapping("/cmp/update")
	public String update(
			@RequestParam(value = "id") int pid,
			HttpSession session,
			Model model
			) {
		Company cmp = (Company)session.getAttribute("cmpInfo");
		if(cmp==null) {
			return "redirect:/cmp/login";
		}
		Optional<Product> p = pdRepo.findById(pid);
		if(p.isPresent()) {
			model.addAttribute("product", p.get());
		}
		return "product_update";
	}
	
	@PostMapping("/cmp/update")
	public String update(
			@RequestParam(value = "id") int pid,
			HttpSession session,
			ProductDTO pd) {
		Company cmp = (Company)session.getAttribute("cmpInfo");
		if(cmp==null) {
			return "redirect:/cmp/login";
		}
		Product p = new Product();
		p.setCmpName(cmp.getCmpName());
		p.setName(pd.getName());
		p.setImg(pd.getImg());
		p.setPrice(pd.getPrice());
		p.setCount(pd.getCount());
		p.setCreateDate(LocalDateTime.now());
		pdRepo.save(p);
		return "redirect:/product/cmp/list";
	}
}
