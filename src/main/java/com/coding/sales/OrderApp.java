package com.coding.sales;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.coding.sales.input.OrderCommand;
import com.coding.sales.input.OrderItemCommand;
import com.coding.sales.input.PaymentCommand;
import com.coding.sales.member.Member;
import com.coding.sales.output.DiscountItemRepresentation;
import com.coding.sales.output.OrderItemRepresentation;
import com.coding.sales.output.OrderRepresentation;
import com.coding.sales.product.DiscountCard;
import com.coding.sales.product.FullReduction;
import com.coding.sales.output.PaymentRepresentation;
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
        System.out.println(result.toString());
        return result.toString();
    }

    OrderRepresentation checkout(OrderCommand command) {
        OrderRepresentation result =null;
        Date createTime=new Date();
        String orderId=command.getOrderId();
        String memberNo=command.getMemberId();
        Member member=getMemberByNo(memberNo);
        
        //获取总金额
        BigDecimal amount=getAmount(command);
        //新增积分
        BigDecimal memberPointsIncreased= member.calculationPointsByLevel(amount);
        //新会员积分
        BigDecimal memberPoints=memberPointsIncreased.add(member.getMemberPoints());
        //新的会员等级
        member.setMemberPoints(memberPoints);
        String oldMemberType=member.getOldMemberType();
        String newMemberType=member.getNewMemberType();
        String memberName=member.getMemberName();
        //订单，明细
        List<OrderItemCommand> items=command.getItems();
        List<OrderItemRepresentation> orderItems=new ArrayList<OrderItemRepresentation>();
        Map<String, Object> resultMap= getOrderItemRepresentations(items, orderItems);
        BigDecimal totalDiscountPrice=(BigDecimal) resultMap.get("totalDiscountPrice");
        BigDecimal totalAmount=(BigDecimal) resultMap.get("totalAmount");
        
        List<DiscountItemRepresentation> discounts=(List<DiscountItemRepresentation>) resultMap.get("discounts");
        //应收金额
        BigDecimal receivables=totalAmount.subtract(totalDiscountPrice);
        List<PaymentCommand> payments=command.getPayments();
        List<PaymentRepresentation> paymentRepresentations=new ArrayList<PaymentRepresentation>();
        for (PaymentCommand paymentCommand:payments) {
        	PaymentRepresentation paymentRepresentation=new PaymentRepresentation(paymentCommand.getType(),paymentCommand.getAmount());
        	paymentRepresentations.add(paymentRepresentation);
        }
        List<String> discountCards=command.getDiscounts();
        
        result=new OrderRepresentation(
        		orderId, createTime,
                 memberNo,memberName, 
                 oldMemberType,
                 newMemberType,
                 Integer.parseInt(memberPointsIncreased.toString()),
                 Integer.parseInt(memberPoints.toString()),
                 orderItems,
                 totalAmount, discounts, totalDiscountPrice,
                 receivables, paymentRepresentations,  discountCards);
        
        /**
         * @param orderId               订单号
         * @param createTime            订单创建时间
         * @param memberNo              会员编号
         * @param memberName            会员姓名
         * @param oldMemberType         原会员等级
         * @param newMemberType         新会员等级。当新老等级不一致时，视为升级
         * @param memberPointsIncreased 本次消费会员新增的积分
         * @param memberPoints          会员最新的积分( = 老积分 + memberPointsIncreased)
         * @param orderItems            订单明细
         * @param totalPrice            订单总金额
         * @param discounts             优惠明细
         * @param totalDiscountPrice    优惠总金额
         * @param receivables           应收金额
         * @param payments              付款记录
         * @param discountCards         付款使用的打折券
         */
        
      /*  public  */
        
        //1.方鼎银行贵金属购买凭证 
        //2.写入订单信息
        //3.写入商品信息
        //4.写入优惠清单
        
        
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

	private Map<String, Object> getOrderItemRepresentations(List<OrderItemCommand> items, List<OrderItemRepresentation> orderItems) {
		
		List<DiscountItemRepresentation> discountItems =new ArrayList<DiscountItemRepresentation>();
		
		BigDecimal totalDiscountPrice=new BigDecimal(0);
		Map<String, Object> map=new HashMap<String, Object>();
		BigDecimal totalAmount=new BigDecimal(0);
		Map<String, Object> productDiscountMap=new HashMap<String, Object>();
 		for(OrderItemCommand orderItemCommand:items){
            ProductInformation productInformation=getProductInformationById(orderItemCommand.getProduct());
            
            //获得 订单明细信息
            String  productNo=productInformation.getProduct();
            String productName=productInformation.getProductName();
            BigDecimal price =productInformation.getPrice();
            BigDecimal count=orderItemCommand.getAmount();
            BigDecimal subTotal=count.multiply(price);
            OrderItemRepresentation orderItemRepresentation= new OrderItemRepresentation(productNo,productName,price,count,subTotal);
            orderItems.add(orderItemRepresentation);
            //根据订单编号查询订单信息
            
            //获取优惠明细
            Map<String, Object> discountMap=productInformation.calculationDiscountAmount(fullReductionMap, discountCardMap, Integer.parseInt(count.toString()));
            
            BigDecimal discountAmount=(BigDecimal) discountMap.get("discountAmount");
            discountItems.add(new DiscountItemRepresentation(productNo,productName,discountAmount));
            totalDiscountPrice.add(discountAmount);
            totalAmount.add(subTotal);
            
            productDiscountMap.put(productNo, discountAmount);
            
            map.put(productNo, discountAmount);
        }
		map.put("totalDiscountPrice", totalDiscountPrice);
		map.put("totalAmount", totalAmount);
		map.put("productDiscountMap", productDiscountMap);
		map.put("discounts", discountItems);
		return map;
	}

	private ProductInformation getProductInformationById(String product) {
		return null;
	}

	private BigDecimal getAmount(OrderCommand command) {
		List<PaymentCommand>  discounts=command.getPayments();
        return discounts.get(0).getAmount();
	}

	private Member getMemberByNo(String memberNo) {
		return null;
	}
}
