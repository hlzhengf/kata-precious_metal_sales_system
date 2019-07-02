package com.coding.sales.product;

import java.math.BigDecimal;

public class FullReduction {
	private String fullReductionName;
	private String type;
	private BigDecimal fullLimit;
	private BigDecimal reduction;
	public String getFullReductionName() {
		return fullReductionName;
	}
	public void setFullReductionName(String fullReductionName) {
		this.fullReductionName = fullReductionName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getFullLimit() {
		return fullLimit;
	}
	public void setFullLimit(BigDecimal fullLimit) {
		this.fullLimit = fullLimit;
	}
	public BigDecimal getReduction() {
		return reduction;
	}
	public void setReduction(BigDecimal reduction) {
		this.reduction = reduction;
	}
	
}
