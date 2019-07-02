package com.coding.sales.member;

import java.math.BigDecimal;

public class Member {

	private static final String DIAMONDSCARD = "钻石卡";
	private static final String PLATINUMCARD = "白金卡";
	private static final String GOLDCARD = "金卡";
	private static final String COMMONCCARD = "普卡";
	private String memberNo;
	private String cardNumber;
	private BigDecimal memberPoints;
	private String memberName;
	private String newMemberType;
	private String oldMemberType;
	 
	public String getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public BigDecimal getMemberPoints() {
		return memberPoints;
	}
	public void setMemberPoints(BigDecimal memberPoints) {
		this.memberPoints = memberPoints;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getNewMemberType() {
		return newMemberType;
	}
	public void setNewMemberType(String newMemberType) {
		this.newMemberType = newMemberType;
	}
	public String getOldMemberType() {
		return oldMemberType;
	}
	public void setOldMemberType(String oldMemberType) {
		this.oldMemberType = oldMemberType;
	}
	@Override
	public String toString() {
		return  memberName+","+oldMemberType+","+cardNumber+","+memberPoints;
	}	
	public   BigDecimal calculationPointsByLevel(BigDecimal amount){
		
		BigDecimal result=new BigDecimal(0);
		if(COMMONCCARD.equals(oldMemberType)){ 
			result.add(amount);
		}
		if(GOLDCARD.equals(oldMemberType)){
			result.add(amount.multiply(new BigDecimal(1.5)));
		}
		if(PLATINUMCARD.equals(oldMemberType)){
			result.add(amount.multiply(new BigDecimal(1.8)));
		}
		if(DIAMONDSCARD.equals(oldMemberType)){
			result.add(amount.multiply(new BigDecimal(2)));
		}
		return result;
	}
	
	public String  getMemberLeve(BigDecimal addPoints){
		BigDecimal totalPoints=memberPoints.add(addPoints);
		 if (totalPoints.compareTo(new BigDecimal(10000)) == -1) {
            return COMMONCCARD;
 
        } else if (totalPoints.compareTo(new BigDecimal(50000)) < 0) {
           return GOLDCARD;
 
        } else if (totalPoints.compareTo(new BigDecimal(100000)) < 0) {
        	return PLATINUMCARD;
 
        } else if (totalPoints.compareTo(new BigDecimal(100000)) > -1) {
        	return DIAMONDSCARD;
 
        }
		 return "";
		
	}
	
}
