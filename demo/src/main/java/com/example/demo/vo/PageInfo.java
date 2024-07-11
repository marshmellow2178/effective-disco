package com.example.demo.vo;

import org.springframework.data.domain.Page;

import com.example.demo.entity.Review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageInfo {
	private int blockSize;
	private int size;
	private int number;
	private long totalElements;
	private int totalPages;
	private boolean hasPrevious;
	private boolean hasNext;
	
	public void setReviewPageInfo(Page<Review> page) {
		this.blockSize = 10;
		this.size = page.getSize();
		this.number = page.getNumber();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
		this.hasPrevious = page.hasPrevious();
		this.hasNext = page.hasNext();
	}
}
