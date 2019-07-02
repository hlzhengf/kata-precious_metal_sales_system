package com.coding.sales.MemberTest.java;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.coding.sales.member.Member;

public class MemberTest {
	
	private Member member;
	@Before
	public void init(){
		member=new Member();
		member.setCardNumber("6236609999");
		member.setMemberName("马丁");
		member.setMemberPoints(new BigDecimal("9860"));
		member.setOldMemberType("普卡");
	}

	 @Test
	 public void should_print_member_information_format(){
		String actualPrintResult =member.toString();
        String expectedResult =getExpectedResult();
        Assert.assertEquals(expectedResult, actualPrintResult);
	 }
	 public String  getExpectedResult(){
		 return "马丁,普卡,6236609999,9860";
	 }
	 
}
