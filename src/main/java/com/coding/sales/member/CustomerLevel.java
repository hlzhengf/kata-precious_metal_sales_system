package com.coding.sales.member;

public enum   CustomerLevel {
	DIAMONDSCARD("钻石卡"),PLATINUMCARD("白金卡"),GOLDCARD("金卡"),COMMONCCARD("普卡");
	private final String name;
    
    private CustomerLevel(String name)
    {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
