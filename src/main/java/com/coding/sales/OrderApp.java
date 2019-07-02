package com.coding.sales;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.coding.sales.input.OrderCommand;
import com.coding.sales.output.OrderRepresentation;
import com.coding.sales.product.DiscountCard;
import com.coding.sales.product.FullReduction;
import com.coding.sales.product.ProductInformation;

/**
 * 销售系统的主入口
 * 用于打印销售凭证
 */
public class OrderApp {

	static Map productMap = new HashMap();
    static Map discountCardMap = new HashMap();
    static Map fullReductionMap = new HashMap();
    static{
    	discountCardMap.put("9折券", new DiscountCard("9折券", 0.9));
    	discountCardMap.put("95折券", new DiscountCard("95折券", 0.95));
    	fullReductionMap.put("每满3000元减350", new FullReduction("每满3000元减350", "0", new BigDecimal("3000"), new BigDecimal("350")));
    	fullReductionMap.put("每满2000元减30", new FullReduction("每满2000元减30", "0", new BigDecimal("2000"), new BigDecimal("30")));
    	fullReductionMap.put("每满1000元减10", new FullReduction("每满1000元减10", "0", new BigDecimal("1000"), new BigDecimal("10")));
    	fullReductionMap.put("第3件半价", new FullReduction("第3件半价", "1", new BigDecimal("3"), new BigDecimal("0.5")));
    	fullReductionMap.put("满3送1", new FullReduction("满3送1", "1", new BigDecimal("4"), new BigDecimal("1")));
    	productMap.put("001001", new ProductInformation("世园会五十国钱币册", "001001", new BigDecimal("998"), "册", null, null));
    	productMap.put("001002", new ProductInformation("2019北京世园会纪念银章大全40g", "001002", new BigDecimal("1380"), "盒", "9折券", null));
    	productMap.put("003001", new ProductInformation("招财进宝", "003001", new BigDecimal("1580"), "条", "95折券", null));
    	productMap.put("003002", new ProductInformation("水晶之恋", "003002", new BigDecimal("980"), "条", null, new String[]{"第3件半价","满3送1"}));
    	productMap.put("002002", new ProductInformation("中国经典钱币套装", "002002", new BigDecimal("998"), "册", null, new String[]{"每满2000元减30","每满1000元减10"}));
    	productMap.put("002001", new ProductInformation("守扩之羽比翼双飞4.8g", "002001", new BigDecimal("1080"), "条", "95折券", new String[]{"第3件半价","满3送1"}));
    	productMap.put("002003", new ProductInformation("中国银象棋12g", "002003", new BigDecimal("698"), "套", "9折券", new String[]{"每满3000元减350","每满2000元减30","每满1000元减10"}));
    }
    public static void main(String[] args) {
    	discountCardMap.get("9折券");
        if (args.length != 2) {
            throw new IllegalArgumentException("参数不正确。参数1为销售订单的JSON文件名，参数2为待打印销售凭证的文本文件名.");
        }

        String jsonFileName = args[0];
        String txtFileName = args[1];

        String orderCommand = FileUtils.readFromFile(jsonFileName);
        OrderApp app = new OrderApp();
        String result = app.checkout(orderCommand);
        FileUtils.writeToFile(result, txtFileName);
    }

    public String checkout(String orderCommand) {
        OrderCommand command = OrderCommand.from(orderCommand);
        OrderRepresentation result = checkout(command);
        
        return result.toString();
    }

    OrderRepresentation checkout(OrderCommand command) {
        OrderRepresentation result = null;
        //
        //1.方鼎银行贵金属购买凭证 
        //2.写入订单信息
        //3.写入商品信息
        //4.写入优惠清单
        //
        
        
        /*
        {
        	  "orderId": "0000001",
        	  "memberId": "6236609999",
        	  "createTime": "2019-07-02 15:00:00",
        	  "items": [
        	    {
        	      "product": "001001",
        	      "amount": 2
        	    },
        	    {
        	      "product": "001002",
        	      "amount": 3
        	    },
        	    {
        	      "product": "002002",
        	      "amount": 1
        	    },
        	    {
        	      "product": "002003",
        	      "amount": 5
        	    }
        	  ],
        	  "payments": [
        	    {
        	      "type": "余额支付",
        	      "amount": 9860.00
        	    }
        	  ],
        	  "discountCards": [
        	    "9折券"
        	  ]
        	}*/
        
		   /*     方鼎银行贵金属购买凭证
		
		        销售单号：0000001 日期：2019-07-02 15:00:00
		        客户卡号：6236609999 会员姓名：马丁 客户等级：金卡 累计积分：19720
		
		        商品及数量           单价         金额
		        (001001)世园会五十国钱币册x2, 998.00, 1996.00
		        (001002)2019北京世园会纪念银章大全40gx3, 1380.00, 4140.00
		        (002002)中国经典钱币套装x1, 998.00, 998.00
		        (002003)中国银象棋12gx5, 698.00, 3490.00
		        合计：10624.00
		
		        优惠清单：
		        (001002)2019北京世园会纪念银章大全40g: -414.00
		        (002003)中国银象棋12g: -350.00
		        优惠合计：764.00
		
		        应收合计：9860.00
		        收款：
		         9折券
		         余额支付：9860.00
		
		        客户等级与积分：
		         新增积分：9860
		         恭喜您升级为金卡客户！*/

        //TODO: 请完成需求指定的功能

        return result;
    }
}
