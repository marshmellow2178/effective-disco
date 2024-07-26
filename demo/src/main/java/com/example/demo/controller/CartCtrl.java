package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Repo.CartRepo;
import com.example.demo.Repo.ProductRepo;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Product;
import com.example.demo.entity.UserInfo;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartCtrl {
	private final CartRepo cartRepo;
	private final ProductRepo pdRepo;
	
	@ResponseBody
	@GetMapping("/add")
	public int addCart(
			HttpSession session,
			@RequestParam(value = "pid") int pid,
			@RequestParam(value = "cnt", defaultValue = "1") int count
			) {
		UserInfo user = (UserInfo)session.getAttribute("userInfo");
		Product p = pdRepo.findById(pid).get();
		List<Cart> cartList = cartRepo.findByUserId(user.getId());
		Cart cart = cartRepo.findByUserIdAndProductId(user.getId(), pid);
		if(cartList.size()!=0){
			if(cart!=null) {
				cart.setCount(cart.getCount()+count);
				cartRepo.save(cart);
				return 0;
			}
			if(p.getCmpName().equals(cartList.get(0).getProduct().getCmpName())) {
				cart = new Cart();
				cart.setCount(count);
				cart.setProduct(p);
				cart.setUser(user);
				cartRepo.save(cart);
				return 0;
			}else {
				return -1;
			}
		}else {
			cart = new Cart();
			cart.setCount(count);
			cart.setProduct(p);
			cart.setUser(user);
			cartRepo.save(cart);
			return 0;
		}
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
