package com.example.demo.controller;

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
import com.example.demo.Repo.OrderDetailRepo;
import com.example.demo.Repo.OrderRepo;
import com.example.demo.Repo.ProductRepo;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Company;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.OrderInfo;
import com.example.demo.entity.Product;
import com.example.demo.entity.UserInfo;
import com.example.demo.vo.GlobalVO;
import com.example.demo.vo.OrderFormVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderCtrl {

	private final OrderRepo orderRepo;
	private final OrderDetailRepo odRepo;
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
	
	@GetMapping("/cancel")
	public String cancel(@RequestParam(value = "id") int orderId) {
		OrderInfo order = orderRepo.findById(orderId);
		List<OrderDetail> odList = odRepo.findByOrderId(orderId);
		for(int i = 0;i<odList.size();i++) {
			OrderDetail od = odList.get(i);
			Product p = pdRepo.findByCmpNameAndName(order.getCmpName(), od.getProductName());
			p.setCount(p.getCount()+od.getProductCount());
			pdRepo.save(p);
			odRepo.delete(od);
		}
		orderRepo.delete(order);
		return "redirect:/order/list";
	}
	
	@GetMapping("/cmp/cancel")
	public String cmpCancel(@RequestParam(value = "id") int orderId) {
		OrderInfo order = orderRepo.findById(orderId);
		orderRepo.delete(order);
		return "redirect:/order/cmp/list";
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
		Page<OrderInfo> orderPage = orderRepo.findByUserId(userId, pageable);
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
			@RequestParam(value = "cnt", defaultValue = "1") int count,
			Model model
			) {
		if(session.getAttribute("userInfo")==null) {
			return "redirect:/login";
		}
		List<Cart> cartList = new ArrayList<>();
		Cart c = new Cart();
		c.setProduct(pdRepo.findById(pid).get());
		c.setCount(c.getCount()+count);
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
		UserInfo user = (UserInfo)session.getAttribute("userInfo");
		if(user==null) {
			return "redirect:/login";
		}
		List<Cart> cartList = new ArrayList<>();
		for(int i = 0;i<pid.length;i++) {
			Cart c = cartRepo.findByUserIdAndProductId(user.getId(), pid[i]);
			c.setCount(cnt[i]);
			cartRepo.save(c);
			cartList.add(c);
		}
		model.addAttribute("cartList", cartList);
		return "order_form";
	}
	
	@PostMapping("/create")
	public String createOrder(
			HttpSession  session,
			OrderFormVO ofvo
			) {
		UserInfo user = (UserInfo)session.getAttribute("userInfo");
		OrderInfo order = new OrderInfo();
		order.setUser(user);
		order.createOrder(ofvo);
		orderRepo.save(order);
		
		int[] pid = ofvo.getPid();
		int[] count = ofvo.getCount();
		for(int i = 1;i<pid.length;i++) {
			Product p = pdRepo.findById(pid[i]).get();
			OrderDetail od = new OrderDetail();
			od.setOrder(order);
			od.setProduct(p);
			od.setProductCount(count[i]);
			p.setCount(p.getCount()-count[i]);
			pdRepo.save(p);
			odRepo.save(od);
			Cart c = cartRepo.findByUserIdAndProductId(user.getId(), pid[i]);
			if(c!=null) {cartRepo.delete(c);}
		}
		return "redirect:/order/list";
	}
}
