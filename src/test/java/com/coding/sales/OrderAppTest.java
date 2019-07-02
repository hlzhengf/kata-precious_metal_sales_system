package com.coding.sales;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.coding.sales.product.DiscountCard;
import com.coding.sales.product.FullReduction;
import com.coding.sales.product.ProductInformation;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class OrderAppTest {
    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        Object[][] data = new Object[][]{
                {"sample_command.json", "sample_result.txt"},
                
        };

        return Arrays.asList(data);
    }

    private String commandFileName;
    private String expectedResultFileName;

    public OrderAppTest(String commandFileName, String expectedResultFileName) {
        this.commandFileName = commandFileName;
        this.expectedResultFileName = expectedResultFileName;
    }
    
    static Map productMap = new HashMap();
    static Map discountCardMap = new HashMap();
    static Map fullReductionMap = new HashMap();
    
    @Before
    public void init(){
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

    @Test
    public void should_checkout_order() {
        String orderCommand = FileUtils.readFromFile(getResourceFilePath(commandFileName));
        OrderApp app = new OrderApp();
        String actualResult = app.checkout(orderCommand);

        String expectedResult = FileUtils.readFromFile(getResourceFilePath(expectedResultFileName));

        assertEquals(expectedResult, actualResult);
    }

    private String getResourceFilePath(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return file.getAbsolutePath();
    }

}
