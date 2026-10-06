package com.bank;

import com.bank.model.Deposit;
import com.bank.model.DepositComparators;
import com.bank.model.DepositType;
import com.bank.parser.DomDepositParser;
import com.bank.parser.SaxDepositParser;
import com.bank.parser.StaxDepositParser;
import com.bank.util.XmlValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    private final String xmlPath = "src/main/resources/bank.xml";
    private final String xsdPath = "src/main/resources/bank.xsd";
    
    private Deposit deposit1;
    private Deposit deposit2;

    @BeforeEach
    void setUp() {
        deposit1 = new Deposit();
        deposit1.setAccountId("ID-9991");
        deposit1.setDepositor("Test Depositor 1");
        deposit1.setAmountOnDeposit(1000.0);
        deposit1.setProfitability(10.0);
        deposit1.setType(DepositType.TERM);

        deposit2 = new Deposit();
        deposit2.setAccountId("ID-9992");
        deposit2.setDepositor("Test Depositor 2");
        deposit2.setAmountOnDeposit(5000.0);
        deposit2.setProfitability(5.0);
        deposit2.setType(DepositType.DEMAND);
    }

    @Test
    void testXmlValidatorWithValidFile() {
        boolean isValid = XmlValidator.validate(xmlPath, xsdPath);
        assertTrue(isValid, "Validator should return true for a valid XML file");
    }

    @Test
    void testDomParser() {
        DomDepositParser parser = new DomDepositParser();
        List<Deposit> deposits = parser.parse(xmlPath);
        
        assertNotNull(deposits);
        assertEquals(3, deposits.size(), "DOM Parser should read exactly 3 deposits");
        assertEquals("ID-1001", deposits.get(0).getAccountId(), "First deposit ID mismatch");
    }

    @Test
    void testSaxParser() {
        SaxDepositParser parser = new SaxDepositParser();
        List<Deposit> deposits = parser.parse(xmlPath);
        
        assertNotNull(deposits);
        assertEquals(3, deposits.size(), "SAX Parser should read exactly 3 deposits");
        assertEquals(DepositType.ACCUMULATIVE, deposits.get(0).getType(), "Enum parsing failed for SAX");
    }

    @Test
    void testStaxParser() {
        StaxDepositParser parser = new StaxDepositParser();
        List<Deposit> deposits = parser.parse(xmlPath);
        
        assertNotNull(deposits);
        assertEquals(3, deposits.size(), "StAX Parser should read exactly 3 deposits");
        assertEquals(15000.50, deposits.get(1).getAmountOnDeposit(), "Double parsing failed for StAX");
    }

    @Test
    void testSortByAmountDescending() {
        List<Deposit> list = Arrays.asList(deposit1, deposit2);
        list.sort(DepositComparators.BY_AMOUNT_DESC);
        
        assertEquals("ID-9992", list.get(0).getAccountId(), "Sorting by amount descending failed");
    }

    @Test
    void testSortByProfitabilityAscending() {
        List<Deposit> list = Arrays.asList(deposit1, deposit2);
        list.sort(DepositComparators.BY_PROFITABILITY);
        
        assertEquals("ID-9992", list.get(0).getAccountId(), "Sorting by profitability ascending failed");
    }

    @Test
    void testXsltTransformer() {
        String xslPath = "src/main/resources/bank.xsl";
        String testOutputPath = "src/main/resources/test_bank_grouped.xml";
        
        com.bank.util.XsltTransformer.transform(xmlPath, xslPath, testOutputPath);

        File outputFile = new File(testOutputPath);
        assertTrue(outputFile.exists(), "Transformed XML file should be created");
        assertTrue(outputFile.length() > 0, "Transformed XML file should not be empty");
        
        outputFile.delete();
    }
}
