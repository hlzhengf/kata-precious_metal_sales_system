package com.coding.sales.member;

public class Member {

	private String memberNo;
	private String cardNumber;
	private String memberPoints;
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
	
	public String getMemberPoints() {
		return memberPoints;
	}
	public void setMemberPoints(String memberPoints) {
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
}
