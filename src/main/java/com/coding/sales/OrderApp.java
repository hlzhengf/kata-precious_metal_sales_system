package com.coding.sales;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
 * 销售系统的主入口 用于打印销售凭证
 */
public class OrderApp {

	static Map productMap = new HashMap();
	static Map discountCardMap = new HashMap();
	static Map fullReductionMap = new HashMap();
	static Map memberMap = new HashMap();
	static {
		discountCardMap.put("9折券", new DiscountCard("9折券", 0.9));
		discountCardMap.put("95折券", new DiscountCard("95折券", 0.95));
		fullReductionMap.put("每满3000元减350",
				new FullReduction("每满3000元减350", "0", new BigDecimal("3000"), new BigDecimal("350")));
		fullReductionMap.put("每满2000元减30",
				new FullReduction("每满2000元减30", "0", new BigDecimal("2000"), new BigDecimal("30")));
		fullReductionMap.put("每满1000元减10",
				new FullReduction("每满1000元减10", "0", new BigDecimal("1000"), new BigDecimal("10")));
		fullReductionMap.put("第3件半价", new FullReduction("第3件半价", "1", new BigDecimal("3"), new BigDecimal("0.5")));
		fullReductionMap.put("满3送1", new FullReduction("满3送1", "1", new BigDecimal("4"), new BigDecimal("1")));
		productMap.put("001001", new ProductInformation("世园会五十国钱币册", "001001", new BigDecimal("998"), "册", null, null));
		productMap.put("001002",
				new ProductInformation("2019北京世园会纪念银章大全40g", "001002", new BigDecimal("1380"), "盒", "9折券", null));
		productMap.put("003001", new ProductInformation("招财进宝", "003001", new BigDecimal("1580"), "条", "95折券", null));
		productMap.put("003002", new ProductInformation("水晶之恋", "003002", new BigDecimal("980"), "条", null,
				new String[] { "第3件半价", "满3送1" }));
		productMap.put("002002", new ProductInformation("中国经典钱币套装", "002002", new BigDecimal("998"), "册", null,
				new String[] { "每满2000元减30", "每满1000元减10" }));
		productMap.put("002001", new ProductInformation("守扩之羽比翼双飞4.8g", "002001", new BigDecimal("1080"), "条", "95折券",
				new String[] { "第3件半价", "满3送1" }));
		productMap.put("002003", new ProductInformation("中国银象棋12g", "002003", new BigDecimal("698"), "套", "9折券",
				new String[] { "每满3000元减350", "每满2000元减30", "每满1000元减10" }));
		memberMap.put("6236609999", new Member("6236609999", "6236609999", new BigDecimal(9860), "马丁", null, "普卡"));
		memberMap.put("6630009999", new Member("6630009999", "6630009999", new BigDecimal(48860), "王立", null, "金卡"));
		memberMap.put("8230009999", new Member("8230009999", "8230009999", new BigDecimal(98860), "李想", null, "白金卡"));
		memberMap.put("9230009999", new Member("9230009999", "9230009999", new BigDecimal(198860), "张三", null, "钻石卡"));
	}

	public static void main(String[] args) {
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
		Date createTime = HandleCreateTime(command);
		
		String orderId = command.getOrderId();
		String memberNo = command.getMemberId();
		Member member = getMemberByNo(memberNo);

		BigDecimal amount = getAmount(command);
		BigDecimal memberPointsIncreased = member.calculationPointsByLevel(amount);
		BigDecimal memberPoints = memberPointsIncreased.add(member.getMemberPoints());
		String oldMemberType = member.getOldMemberType();
		String newMemberType = member.getMemberLeve(memberPoints);
		String memberName = member.getMemberName();
		
		List<OrderItemCommand> items = command.getItems();
		List<OrderItemRepresentation> orderItems = new ArrayList<OrderItemRepresentation>();
		Map<String, Object> resultMap = getOrderItemRepresentations(items, orderItems,command);
		BigDecimal totalDiscountPrice = (BigDecimal) resultMap.get("totalDiscountPrice");
		BigDecimal totalAmount = (BigDecimal) resultMap.get("totalAmount");

		List<DiscountItemRepresentation> discounts = (List<DiscountItemRepresentation>) resultMap.get("discounts");
		BigDecimal receivables = totalAmount.subtract(totalDiscountPrice);
		List<PaymentCommand> payments = command.getPayments();
		List<PaymentRepresentation> paymentRepresentations = new ArrayList<PaymentRepresentation>();
		paymentCommandToPaymentRepresentation(payments, paymentRepresentations);
		
		List<String> discountCards = (List<String>) resultMap.get("discountCards");

		result = new OrderRepresentation(orderId, createTime, memberNo, memberName, oldMemberType, newMemberType,
				memberPointsIncreased.intValue(), memberPoints.intValue(), orderItems, totalAmount, discounts,
				totalDiscountPrice, receivables, paymentRepresentations, discountCards);
		return result;
	}

	private void paymentCommandToPaymentRepresentation(List<PaymentCommand> payments,
			List<PaymentRepresentation> paymentRepresentations) {
		for (PaymentCommand paymentCommand : payments) {
			PaymentRepresentation paymentRepresentation = new PaymentRepresentation(paymentCommand.getType(),
					paymentCommand.getAmount());
			paymentRepresentations.add(paymentRepresentation);
		}
	}

	private Date HandleCreateTime(OrderCommand command) {
		Date  createTime=null;
		try {
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			createTime = simpleDateFormat.parse(command.getCreateTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return createTime;
	}

	private Map<String, Object> getOrderItemRepresentations(List<OrderItemCommand> items,
			List<OrderItemRepresentation> orderItems,OrderCommand command) {

		List<DiscountItemRepresentation> discountItems = new ArrayList<DiscountItemRepresentation>();

		BigDecimal totalDiscountPrice = new BigDecimal(0);
		Map<String, Object> map = new HashMap<String, Object>();
		BigDecimal totalAmount = new BigDecimal(0);
		Map<String, Object> productDiscountMap = new HashMap<String, Object>();
		List<String> discountCards = new ArrayList<String>();
		for (OrderItemCommand orderItemCommand : items) {
			ProductInformation productInformation = getProductInformationById(orderItemCommand.getProduct());

			String productNo = productInformation.getProduct();
			String productName = productInformation.getProductName();
			BigDecimal price = productInformation.getPrice();
			BigDecimal count = orderItemCommand.getAmount();
			BigDecimal subTotal = count.multiply(price);
			OrderItemRepresentation orderItemRepresentation = new OrderItemRepresentation(productNo, productName, price,
					count, subTotal);
			orderItems.add(orderItemRepresentation);

			Map<String, Object> discountMap = productInformation.calculationDiscountAmount(fullReductionMap,
					discountCardMap, Integer.parseInt(count.toString()),command.getDiscounts());
			
			String discountMethod = (String)discountMap.get("method");
			Object discountcard = discountCardMap.get(discountMethod);
			if(null!=discountcard) discountCards.add(discountMethod);

			BigDecimal discountAmount = (BigDecimal) discountMap.get("discountAmount") == null ? new BigDecimal(0)
					: (BigDecimal) discountMap.get("discountAmount");
			if ((BigDecimal) discountMap.get("discountAmount") != null) {
				discountItems.add(new DiscountItemRepresentation(productNo, productName, discountAmount));
			}
			totalDiscountPrice = totalDiscountPrice.add(discountAmount);
			totalAmount = totalAmount.add(subTotal);
			productDiscountMap.put(productNo, discountAmount);
			map.put(productNo, discountAmount);
		}
		map.put("totalDiscountPrice", totalDiscountPrice);
		map.put("totalAmount", totalAmount);
		map.put("productDiscountMap", productDiscountMap);
		map.put("discounts", discountItems);
		map.put("discountCards", discountCards);
		return map;
	}

	private ProductInformation getProductInformationById(String product) {
		return (ProductInformation) productMap.get(product);
	}

	private BigDecimal getAmount(OrderCommand command) {
		List<PaymentCommand> discounts = command.getPayments();
		return discounts.get(0).getAmount();
	}

	private Member getMemberByNo(String memberNo) {
		return (Member) memberMap.get(memberNo);
	}
}
