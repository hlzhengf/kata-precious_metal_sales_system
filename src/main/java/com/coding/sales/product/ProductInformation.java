package com.coding.sales.product;

import java.math.BigDecimal;
import java.util.List;

public class ProductInformation {
	private String 	productName;
	private String 	product;
	private BigDecimal 	price;
	private String 	unit;
	private List<Discount> discountList;
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
	public List<Discount> getDiscountList() {
		return discountList;
	}
	public void setDiscountList(List<Discount> discountList) {
		this.discountList = discountList;
	}
}
