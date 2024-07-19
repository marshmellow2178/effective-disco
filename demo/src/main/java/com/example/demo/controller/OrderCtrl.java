package com.example.demo.controller;

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
import com.example.demo.Repo.OrderRepo;
import com.example.demo.entity.Company;
import com.example.demo.entity.OrderInfo;
import com.example.demo.entity.UserInfo;
import com.example.demo.vo.GlobalVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderCtrl {

	private final OrderRepo orderRepo;
	
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
		String cmpId = ((Company)session.getAttribute("cmpInfo")).getCmpId();
		Page<OrderInfo> orderPage = orderRepo.findByCmpIdAndStateNot(cmpId, 2, pageable);
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
		String cmpId = ((Company)session.getAttribute("cmpInfo")).getCmpId();
		Page<OrderInfo> orderPage = orderRepo.findByCmpIdAndState(cmpId,2, pageable);
		model.addAttribute("orderPage", orderPage);
		return "sales_list";
	}
	
	@PostMapping("/create")
	public String createOrder(
			HttpSession  session
			) {
		if(session.getAttribute("userInfo")==null) {
			return "redirect:/login";
		}
		//우저, 회사, 상품 DTO
		return "redirect:/order_list";
	}
}
