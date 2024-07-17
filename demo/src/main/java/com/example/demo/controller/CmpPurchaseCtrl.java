package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Repo.PurchaseRepo;
import com.example.demo.entity.Purchase;
import com.example.demo.vo.PurchaseDTO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cmp/purchase")
public class CmpPurchaseCtrl {

	private final PurchaseRepo pcRepo;
	
	@ResponseBody
	@GetMapping("/list")
	public List<PurchaseDTO> getPurchaseList(
			HttpSession session,
			@RequestParam(value = "id") int orderId
			){
		List<Purchase> pcList = pcRepo.findByOrderId(orderId);
		List<PurchaseDTO> pvoList = new ArrayList<PurchaseDTO>();
		for(int i = 0;i<pcList.size();i++) {
			Purchase pc = pcList.get(i);
			PurchaseDTO pdto = new PurchaseDTO();
			pdto.setName(pc.getName());
			pdto.setCount(pc.getCount());
			pdto.setPrice(pc.getPrice());
			pvoList.add(pdto);
		} //for fetch lazy
		return pvoList;
	}
}
