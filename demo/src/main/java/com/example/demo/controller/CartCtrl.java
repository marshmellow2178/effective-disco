package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Repo.CartRepo;
import com.example.demo.entity.Cart;
import com.example.demo.entity.UserInfo;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartCtrl {
	private final CartRepo cartRepo;
	
	@ResponseBody
	@PostMapping("/add")
	public void addCart(
			HttpSession session,
			@RequestParam(value = "id") int pid,
			@RequestParam(value = "cnt", defaultValue = "1") int count
			) {
		UserInfo user = (UserInfo)session.getAttribute("userInfo");
		Cart cart = cartRepo.findByUserIdAndProductId(user.getId(), pid);
		if(cart!=null) {
			cart.setCount(cart.getCount()+count);
		}else {
			cart = new Cart();
			cart.setCount(count);
			cart.setProductId(pid);
			cart.setUserId(user.getId());
		}
		cartRepo.save(cart);
	}
	
	@GetMapping("/list")
	public String getCartList(
			HttpSession session,
			@RequestParam(value = "page", defaultValue = "1") int page,
			Model model
			) {
		if(session.getAttribute("userInfo")==null) {
			return "redirect:/login";
		}
		if(page <= 1) { page = 1; }
		Pageable pageable = PageRequest.of(page-1, 9);
		String userId = ((UserInfo)session.getAttribute("userInfo")).getId();
		Page<Cart> cartPage = cartRepo.findByUserId(userId, pageable);
		model.addAttribute("cartPage", cartPage);
		return "cart_list";
	}
	
	@GetMapping("/del")
	public String delCart(
			HttpSession session,
			@RequestParam(value = "id") int cartId
			) {
		Cart cart = cartRepo.findById(cartId).get();
		cartRepo.delete(cart);
		return "redirect:/cart/list";
	}
}
