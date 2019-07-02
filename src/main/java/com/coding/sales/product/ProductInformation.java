package com.coding.sales.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	public Map calculationDiscountAmount(Map fullReductionMap,Map discountCardMap,int amount){
		BigDecimal discountAmount = new BigDecimal("0");
		BigDecimal fullReducAmount = new BigDecimal("0");
		Map compare = new HashMap();
		List list = new ArrayList();
		BigDecimal total = price.multiply(new BigDecimal(amount));
		if(!StringUtil.isNullOrEmpty(discountCard)){
			DiscountCard discount = (DiscountCard)discountCardMap.get(discountCard);
			discountAmount = total.multiply(new BigDecimal(1-discount.getDiscountNum()));
			compare.put(discountAmount, discountCard);
			list.add(discountAmount);
		}
		if(fullReductions != null && fullReductions.length>0){
			for (String fullRed : fullReductions) {
				FullReduction fullRedEntity = (FullReduction)fullReductionMap.get(fullRed);
				BigDecimal fullLimit = fullRedEntity.getFullLimit();
				BigDecimal reduction = fullRedEntity.getReduction();
				if("0".equals(fullRedEntity.getType())){
					if(total.compareTo(fullLimit) ==1){
						fullReducAmount = reduction;
					}
				}else if("1".equals(fullRedEntity.getType())){
					if(new BigDecimal(amount).compareTo(fullRedEntity.getFullLimit()) == 1)
						fullReducAmount = price.subtract(fullRedEntity.getReduction());
				}
				if(fullReducAmount.compareTo(new BigDecimal(0))==1){
					compare.put(fullReducAmount,fullRed);
					list.add(fullReducAmount);
				}
			}
		}
		Map discountInfo = new HashMap();
		if(list.size()>0){
			if(compare.size()>0)
				Collections.sort(list);
			discountInfo.put("method", compare.get((BigDecimal) list.get(list.size()-1)));
			discountInfo.put("discountAmount", (BigDecimal) list.get(list.size()-1));
		}
		
		return discountInfo;
	}
	
}
