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
	/*{"sample_command.json", "sample_result.txt"}*/
    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        Object[][] data = new Object[][]{
        	{"sample_command.json", "sample_result.txt"},
            {"One_command.json", "TestOne_result.txt"},
            {"two_Input.json", "two_Output.txt"},
            {"three_Input.json", "three_Output.txt"}
                
        };

        return Arrays.asList(data);
    }

    private String commandFileName;
    private String expectedResultFileName;

    public OrderAppTest(String commandFileName, String expectedResultFileName) {
        this.commandFileName = commandFileName;
        this.expectedResultFileName = expectedResultFileName;
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
