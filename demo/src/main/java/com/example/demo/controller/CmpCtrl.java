package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Repo.BrandRepo;
import com.example.demo.Repo.CompanyRepo;
import com.example.demo.Repo.ProductRepo;
import com.example.demo.Repo.ReviewRepo;
import com.example.demo.entity.Brand;
import com.example.demo.entity.Company;
import com.example.demo.entity.Product;
import com.example.demo.entity.Review;
import com.example.demo.vo.PlaceVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cmp")
public class CmpCtrl {
	
	private final CompanyRepo cmpRepo;
	private final BrandRepo bRepo;
	private final ProductRepo pdRepo;
	private final ReviewRepo rRepo;
	
	@GetMapping("/login")
	public String cmpLogin() {
		return "cmp_login_form";
	}
	
	@PostMapping("/login")
	public String cmpLogin(
			@RequestParam(value = "cmp_id") String cmpId,
			@RequestParam(value = "cmp_pwd") String cmpPwd,
			HttpSession session) {
		session.removeAttribute("userInfo");
		Company cmp = cmpRepo.findByCmpIdAndPwd(cmpId, cmpPwd);
		if(cmp==null) {
			return "redirect:/cmp/login";
		}
		session.setAttribute("cmpInfo", cmp);
		return "redirect:/cmp";
	}
	
	@GetMapping("/logout")
	public String cmpLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@GetMapping("/mypage")
	public String cmpPage(
			HttpSession session) {
		Company cmpInfo = (Company)session.getAttribute("cmpInfo");
		if(cmpInfo==null) {
			return "redirect:/cmp/login";
		}
		return "mypage";
	}
	
	@GetMapping("/open")
	public String open(HttpSession session) {
		Company cmp = (Company)session.getAttribute("cmpInfo");
		cmp.setState("o");
		session.setAttribute("cmpInfo", cmpRepo.save(cmp));
		return "redirect:/cmp";
	}
	
	@GetMapping("/close")
	public String close(HttpSession session) {
		Company cmp = (Company)session.getAttribute("cmpInfo");
		cmp.setState("c");
		session.setAttribute("cmpInfo", cmpRepo.save(cmp));
		return "redirect:/cmp";
	}

	@ResponseBody
	@PostMapping("/save")
	public void save(
			@RequestBody PlaceVO pvo) {
		Optional<Brand> option = bRepo.findById(pvo.getBrand());
		if(option.isEmpty()) {
			Brand brand = new Brand();
			brand.setCtgr(pvo.getCtgr());
			brand.setName(pvo.getBrand());
			bRepo.save(brand);
		}
		Optional<Company> cOption = cmpRepo.findById(pvo.getName());
		if(cOption.isEmpty()) {
			Company cmp = new Company();
			cmp.setCmpName(pvo.getName());
			cmp.setBrandName(pvo.getBrand());
			cmp.setCtgrName(pvo.getCtgr());
			cmp.setCmpId(pvo.getName());
			cmp.setCmpAddr(pvo.getAddr());
			cmp.setCmpLat(pvo.getLatitude());
			cmp.setCmpLng(pvo.getLongitude());
			cmp.setPwd("1234");
			cmp.setCmpId(pvo.getName());
			cmp.setScore(0.0);
			cmp.setState("c");
			cmpRepo.save(cmp);
		}
	}
	
	@ResponseBody
	@GetMapping("/check")
	public int check(
			@RequestParam(value="name")String cmpName,
			HttpSession session
			) {
		Company cmp = cmpRepo.findByCmpName(cmpName);
		session.removeAttribute("cmp");
		session.setAttribute("cmp", cmpName);
		if(cmp==null) {
			return 0;
		}
		return 1;
	}
	
	@ResponseBody
	@GetMapping("/get")
	public Company getCmp(
			@RequestParam(value="name")String cmpName
			) {
		List<String> ctgrNames = new ArrayList<>();
		ctgrNames.add("ce7");
		ctgrNames.add("ct1");
		ctgrNames.add("fd6");
		ctgrNames.add("mt1");
		ctgrNames.add("pm9");
		ctgrNames.add("cs2");
		Company cmp = cmpRepo.findByCmpName(cmpName);
		Random r = new Random();
		int randNum = r.nextInt(40);
		String pname = "product_"+cmp.getCtgrName().substring(0, 2)+".png";
		if(ctgrNames.contains(cmp.getCtgrName())) {
			List<Product> pList = pdRepo.findByCmpName(cmpName);
			if(pList.size()==0) {
				for(int i = 0;i<=randNum;i++) {
					Product p = new Product();
					p.setCmpName(cmpName);
					p.setName("상품"+i);
					p.setCount(100);
					p.setCreateDate(LocalDateTime.now());
					p.setPrice(1000*i);
					p.setImg(pname);
					pdRepo.save(p);
				}
			}else {
				for(Product p: pList){
					if(p.getImg().equals(pname)) {break;}
					p.setImg(pname);
					pdRepo.save(p);
				}
			}
		}	
		
		List<Review> rList = rRepo.findByCmpName(cmpName);
		if(rList.size()==0) {
			randNum = r.nextInt(25)+1;
			for(int i = 1;i<=randNum;i++) {
				Review review = new Review();
				review.setCmpName(cmp.getCmpName());
				review.setCreateDate(LocalDateTime.now());
				review.setUid("user"+i);
				int scoreNum = r.nextInt(5)+1;
				review.setScore(scoreNum);
				try {
					rRepo.save(review);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}else if(cmp.getScore()==0){
			int totalScore = 0;
			for(int i = 0;i<rList.size();i++) {
				totalScore += rList.get(i).getScore();
			}
			double tempScore = (double)totalScore;
			tempScore /=rList.size();
			tempScore = Math.round(tempScore*100);
			cmp.setScore(tempScore/100);
		}
		
		cmp.setCmpReviewCnt(rList.size());
		cmpRepo.save(cmp);
		return cmp;
	}
	
	@GetMapping("/route")
	public String findRoute(
			@RequestParam(value="sname", defaultValue = "현재 위치")String startName,
			@RequestParam(value="ename")String cmpName,
			HttpSession session,
			Model model) {
		if(!startName.equals("현재 위치")) {
			Company start = cmpRepo.findByCmpName(startName);
			model.addAttribute("start", start);
		}
		Company end = cmpRepo.findByCmpName(cmpName);
		session.setAttribute("cmp", cmpName);
		model.addAttribute("end", end);
		return "cmp_route";
	}
}
