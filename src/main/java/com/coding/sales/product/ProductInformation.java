package com.coding.sales.product;

import java.math.BigDecimal;

public class ProductInformation {
	private String productName;
	private String product;
	private BigDecimal price;
	private String unit;
	private DiscountCard discountCard;
	private FullReduction fullReduction;
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public DiscountCard getDiscountCard() {
		return discountCard;
	}
	public void setDiscountCard(DiscountCard discountCard) {
		this.discountCard = discountCard;
	}
	public FullReduction getFullReduction() {
		return fullReduction;
	}
	public void setFullReduction(FullReduction fullReduction) {
		this.fullReduction = fullReduction;
	}
}
