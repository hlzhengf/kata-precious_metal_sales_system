package com.coding.sales.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.coding.sales.StringUtil;

public class ProductInformation {
	private String productName;
	private String product;
	private BigDecimal price;
	private String unit;
	private String discountCard;
	private String[] fullReductions;
	
	public ProductInformation(String productName, String product, BigDecimal price, String unit,
			String discountCard, String[] fullReductions) {
		super();
		this.productName = productName;
		this.product = product;
		this.price = price;
		this.unit = unit;
		this.discountCard = discountCard;
		this.fullReductions = fullReductions;
	}
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
	public String getDiscountCard() {
		return discountCard;
	}
	public void setDiscountCard(String discountCard) {
		this.discountCard = discountCard;
	}
	public String[] getFullReductions() {
		return fullReductions;
	}
	public void setFullReductions(String[] fullReductions) {
		this.fullReductions = fullReductions;
	}
	//计算优惠钱数
	public BigDecimal calculationDiscountAmount(Map fullReductionMap,Map discountCardMap,int amount){
		BigDecimal discountAmount = new BigDecimal("0");
		BigDecimal fullReducAmount = new BigDecimal("0");
		List compare = new ArrayList();
		if(!StringUtil.isNullOrEmpty(discountCard)){
			DiscountCard discount = (DiscountCard)discountCardMap.get(discountCard);
			discountAmount = price.multiply(new BigDecimal(amount)).multiply(new BigDecimal(discount.getDiscountNum()));
			compare.add(discountAmount);
		}
		BigDecimal total = price.multiply(new BigDecimal(amount));
		if(fullReductions != null && fullReductions.length>0){
			for (String fullRed : fullReductions) {
				FullReduction fullRedEntity = (FullReduction)fullReductionMap.get(fullRed);
				BigDecimal fullLimit = fullRedEntity.getFullLimit();
				BigDecimal reduction = fullRedEntity.getReduction();
				if("0".equals(fullRedEntity.getType())){
					if(total.compareTo(fullLimit) ==1){
						fullReducAmount = total.subtract(reduction);
					}
				}else if("1".equals(fullRedEntity.getType())){
					if(new BigDecimal(amount).compareTo(fullRedEntity.getFullLimit()) == 1)
						fullReducAmount = price.multiply(new BigDecimal(amount).subtract(fullRedEntity.getReduction()));
				}
				compare.add(fullReducAmount);
			}
		}
		if(compare.size()>0)
			Collections.sort(compare);
		
		return (BigDecimal) compare.get(compare.size()-1);
	}
	
}
