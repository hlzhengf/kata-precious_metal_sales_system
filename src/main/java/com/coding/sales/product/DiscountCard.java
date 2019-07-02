package com.coding.sales.product;

public class DiscountCard {
	private String discountCardName;
	private Double discountNum;
	
	public DiscountCard(String discountCardName, Double discountNum) {
		super();
		this.discountCardName = discountCardName;
		this.discountNum = discountNum;
	}
	public String getDiscountCardName() {
		return discountCardName;
	}
	public void setDiscountCardName(String discountCardName) {
		this.discountCardName = discountCardName;
	}
	public Double getDiscountNum() {
		return discountNum;
	}
	public void setDiscountNum(Double discountNum) {
		this.discountNum = discountNum;
	}
}
