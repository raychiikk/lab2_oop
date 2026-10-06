package com.bank.parser;

import com.bank.model.Deposit;
import com.bank.model.DepositType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

public class DomDepositParser implements DepositParser {
    private static final Logger logger = LogManager.getLogger(DomDepositParser.class);
    private final DocumentBuilder documentBuilder;

    public DomDepositParser() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            documentBuilder = factory.newDocumentBuilder();
        } catch (Exception e) {
            logger.error("Error creating DocumentBuilder", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Deposit> parse(String filePath) {
        List<Deposit> deposits = new ArrayList<>();
        try {
            Document document = documentBuilder.parse(filePath);
            document.getDocumentElement().normalize();

            NodeList depositNodes = document.getElementsByTagName(BankXmlTag.DEPOSIT.getValue());

            for (int i = 0; i < depositNodes.getLength(); i++) {
                Node node = depositNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    deposits.add(buildDeposit(element));
                }
            }
            logger.info("DOM Parser successfully parsed {} deposits.", deposits.size());
        } catch (Exception e) {
            logger.error("DOM Parsing error", e);
        }
        return deposits;
    }

    private Deposit buildDeposit(Element element) {
        Deposit deposit = new Deposit();
        deposit.setAccountId(element.getAttribute(BankXmlTag.ACCOUNT_ID.getValue()));
        deposit.setName(getElementTextContent(element, BankXmlTag.NAME.getValue()));
        deposit.setCountry(getElementTextContent(element, BankXmlTag.COUNTRY.getValue()));
        deposit.setType(DepositType.fromValue(getElementTextContent(element, BankXmlTag.TYPE.getValue())));
        deposit.setDepositor(getElementTextContent(element, BankXmlTag.DEPOSITOR.getValue()));
        deposit.setAmountOnDeposit(Double.parseDouble(getElementTextContent(element, BankXmlTag.AMOUNT_ON_DEPOSIT.getValue())));
        deposit.setProfitability(Double.parseDouble(getElementTextContent(element, BankXmlTag.PROFITABILITY.getValue())));
        deposit.setTimeConstraints(Integer.parseInt(getElementTextContent(element, BankXmlTag.TIME_CONSTRAINTS.getValue())));
        return deposit;
    }

    private String getElementTextContent(Element element, String elementName) {
        NodeList nList = element.getElementsByTagName(elementName);
        Node node = nList.item(0);
        return node.getTextContent();
    }
}