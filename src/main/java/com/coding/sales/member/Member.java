package com.coding.sales.member;

import java.math.BigDecimal;

public class Member {

	private static final int ONEHUNDREDTHOUSAND = 100000;
	private static final int FIFTYTHOUSAND = 50000;
	private static final int TENTHOUSAND = 10000;
	private String memberNo;
	private String cardNumber;
	private BigDecimal memberPoints;
	private String memberName;
	private String newMemberType;
	private String oldMemberType;
	public Member(){}
	public Member(String memberNo, String cardNumber, BigDecimal memberPoints, String memberName, String newMemberType,
			String oldMemberType) {
		super();
		this.memberNo = memberNo;
		this.cardNumber = cardNumber;
		this.memberPoints = memberPoints;
		this.memberName = memberName;
		this.newMemberType = newMemberType;
		this.oldMemberType = oldMemberType;
	}
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
		
		BigDecimal result=BigDecimal.ZERO;
		if(CustomerLevel.COMMONCCARD.getName().equals(oldMemberType)){ 
			result=result.add(amount);
		}
		if(CustomerLevel.GOLDCARD.getName().equals(oldMemberType)){
			result=result.add(amount.multiply(new BigDecimal(1.5)));
		}
		if(CustomerLevel.PLATINUMCARD.getName().equals(oldMemberType)){
			result=result.add(amount.multiply(new BigDecimal(1.8)));
		}
		if(CustomerLevel.DIAMONDSCARD.getName().equals(oldMemberType)){
			result=result.add(amount.multiply(new BigDecimal(2)));
		}
		return result;
	}
	
	public String  getMemberLeve(BigDecimal points){
		 if (points.compareTo(new BigDecimal(TENTHOUSAND)) == -1) {
            return CustomerLevel.COMMONCCARD.getName();
 
        } else if (points.compareTo(new BigDecimal(FIFTYTHOUSAND)) < 0) {
           return CustomerLevel.GOLDCARD.getName();
 
        } else if (points.compareTo(new BigDecimal(ONEHUNDREDTHOUSAND)) < 0) {
        	return CustomerLevel.PLATINUMCARD.getName();
 
        } else if (points.compareTo(new BigDecimal(ONEHUNDREDTHOUSAND)) > -1) {
        	return CustomerLevel.DIAMONDSCARD.getName();
 
        }
		 return "";
		
	}
	
}
