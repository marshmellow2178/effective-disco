package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.Repo.OrderRepo;
import com.example.demo.entity.Company;
import com.example.demo.entity.OrderInfo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cmp/order")
public class CmpOrderCtrl {

	private final OrderRepo orderRepo;
	
	@GetMapping("/accept")
	public String accept(@RequestParam(value = "id") int orderId) {
		OrderInfo order = orderRepo.findById(orderId);
		order.setState(1);
		orderRepo.save(order);
		return "redirect:/cmp/order/list";
	}
	
	@GetMapping("/done")
	public String done(@RequestParam(value = "id") int orderId) {
		OrderInfo order = orderRepo.findById(orderId);
		order.setState(2);
		orderRepo.save(order);
		return "redirect:/cmp/order/list";
	}
	
	@GetMapping("/cancel")
	public String cancel(@RequestParam(value = "id") int orderId) {
		OrderInfo order = orderRepo.findById(orderId);
		orderRepo.delete(order);
		return "redirect:/cmp/order/list";
	}
	
	@GetMapping("/list")
	public String getOrderList(HttpSession session,
			@RequestParam(value = "page", defaultValue = "1") int page,
			Model model) {
		if(session.getAttribute("cmpInfo")==null) {
			return "redirect:/cmp/login";
		}
		if(page <= 1) { page = 1; }
		Pageable pageable = PageRequest.of(page-1, 9, Direction.DESC, "date");
		String cmpId = ((Company)session.getAttribute("cmpInfo")).getId();
		Page<OrderInfo> orderPage = orderRepo.findByCmpIdAndStateNot(cmpId, 2, pageable);
		model.addAttribute("orderPage", orderPage);
		return "cmp_order";
	}
	
	@GetMapping("/sales")
	public String getSalesList(HttpSession session,
			@RequestParam(value = "page", defaultValue = "1") int page,
			Model model) {
		if(session.getAttribute("cmpInfo")==null) {
			return "redirect:/cmp/login";
		}
		if(page <= 1) { page = 1; }
		Pageable pageable = PageRequest.of(page-1, 10, Direction.DESC, "date");
		String cmpId = ((Company)session.getAttribute("cmpInfo")).getId();
		Page<OrderInfo> orderPage = orderRepo.findByCmpIdAndState(cmpId,2, pageable);
		model.addAttribute("orderPage", orderPage);
		return "sales_list";
	}
}
