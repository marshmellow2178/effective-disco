package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

import com.example.demo.Repo.CartRepo;
import com.example.demo.Repo.OrderRepo;
import com.example.demo.Repo.ProductRepo;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Company;
import com.example.demo.entity.OrderInfo;
import com.example.demo.entity.Product;
import com.example.demo.entity.UserInfo;
import com.example.demo.vo.GlobalVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderCtrl {

	private final OrderRepo orderRepo;
	private final ProductRepo pdRepo;
	private final CartRepo cartRepo;
	
	@GetMapping("/cmp/accept")
	public String accept(@RequestParam(value = "id") int orderId) {
		OrderInfo order = orderRepo.findById(orderId);
		order.setState(GlobalVO.ORDER_ACCEPT);
		orderRepo.save(order);
		return "redirect:/order/cmp/list";
	}
	
	@GetMapping("/cmp/done")
	public String done(@RequestParam(value = "id") int orderId) {
		OrderInfo order = orderRepo.findById(orderId);
		order.setState(GlobalVO.ORDER_DONE);
		orderRepo.save(order);
		return "redirect:/order/cmp/list";
	}
	
	@GetMapping("/cmp/cancel")
	public String cmpCancel(@RequestParam(value = "id") int orderId) {
		OrderInfo order = orderRepo.findById(orderId);
		orderRepo.delete(order);
		return "redirect:/order/cmp/list";
	}
	
	@GetMapping("/cancel")
	public String cancel(@RequestParam(value = "id") int orderId) {
		OrderInfo order = orderRepo.findById(orderId);
		orderRepo.delete(order);
		return "redirect:/order/list";
	}
	
	@GetMapping("/cmp/list")
	public String cmpOrderList(HttpSession session,
			@RequestParam(value = "page", defaultValue = "1") int page,
			Model model) {
		if(session.getAttribute("cmpInfo")==null) {
			return "redirect:/cmp/login";
		}
		if(page <= 1) { page = 1; }
		Pageable pageable = PageRequest.of(page-1, 9, Direction.DESC, "date");
		String cmpName = ((Company)session.getAttribute("cmpInfo")).getCmpName();
		Page<OrderInfo> orderPage = orderRepo.findByCmpNameAndStateNot(cmpName, 2, pageable);
		model.addAttribute("orderPage", orderPage);
		return "cmp_order";
	}
	
	@GetMapping("/list")
	public String getOrderList(HttpSession session,
			@RequestParam(value = "page", defaultValue = "1") int page,
			Model model) {
		if(session.getAttribute("userInfo")==null) {
			return "redirect:/login";
		}
		if(page <= 1) { page = 1; }
		Pageable pageable = PageRequest.of(page-1, 9, Direction.DESC, "date");
		String userId = ((UserInfo)session.getAttribute("userInfo")).getId();
		Page<OrderInfo> orderPage = orderRepo. findByUserId(userId, pageable);
		model.addAttribute("orderPage", orderPage);
		return "order_list";
	}
	
	@GetMapping("/cmp/sales")
	public String getSalesList(HttpSession session,
			@RequestParam(value = "page", defaultValue = "1") int page,
			Model model) {
		if(session.getAttribute("cmpInfo")==null) {
			return "redirect:/cmp/login";
		}
		if(page <= 1) { page = 1; }
		Pageable pageable = PageRequest.of(page-1, 10, Direction.DESC, "date");
		String cmpName = ((Company)session.getAttribute("cmpInfo")).getCmpName();
		Page<OrderInfo> orderPage = orderRepo.findByCmpNameAndState(cmpName,GlobalVO.ORDER_DONE, pageable);
		model.addAttribute("orderPage", orderPage);
		return "sales_list";
	}
	
	@GetMapping("/instant") //바로구매
	public String createInstant(
			HttpSession  session,
			@RequestParam(value = "pid") int pid,
			Model model
			) {
		if(session.getAttribute("userInfo")==null) {
			return "redirect:/login";
		}
		List<Cart> cartList = new ArrayList<>();
		Cart c = new Cart();
		c.setProduct(pdRepo.findById(pid).get());
		c.setCount(1);
		cartList.add(c);
		model.addAttribute("cartList", cartList);
		return "order_form";
	}
	
	@PostMapping("/cart") //장바구니 구매
	public String createOrder(
			HttpSession  session,
			@RequestParam(value = "pid") int[] pid,
			@RequestParam(value = "count") int[] cnt,
			Model model
			) {
		if(session.getAttribute("userInfo")==null) {
			return "redirect:/login";
		}
		List<Cart> cartList = new ArrayList<>();
		for(int i = 1;i<pid.length;i++) {
			Cart c = new Cart();
			c.setProduct(pdRepo.findById(pid[i]).get());
			c.setCount(cnt[i]);
			cartList.add(c);
		}
		model.addAttribute("cartList", cartList);
		return "order_form";
	}
	
	@PostMapping("/create")
	public String createOrder(
			HttpSession  session,
			@RequestParam(value = "pid") int[] pid,
			@RequestParam(value = "count") int[] count
			) {
		UserInfo user = (UserInfo)session.getAttribute("userInfo");
		for(int i = 1;i<pid.length;i++) {
			Cart c = cartRepo.findByUserIdAndProductId(user.getId(), pid[i]);
			if(c!=null) {
				cartRepo.delete(c);
			}
			Product p = pdRepo.findById(pid[i]).get();
			OrderInfo order = new OrderInfo();
			order.setUserId(user.getId());
			order.setCmpName(p.getCmpName());
			order.setDate(LocalDateTime.now());
			order.setProductId(pid[i]);
			order.setProductCount(count[i]);
			order.setProductName(p.getName());
			order.setProductPrice(p.getPrice());
			order.setPrice(count[i] * p.getPrice());
			orderRepo.save(order);
			p.setCount(p.getCount()-count[i]);
			pdRepo.save(p);
		}
		return "redirect:/order/list";
	}
}
