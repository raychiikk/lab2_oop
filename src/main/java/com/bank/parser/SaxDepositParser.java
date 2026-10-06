package com.bank.parser;

import com.bank.model.Deposit;
import com.bank.model.DepositType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.List;

public class SaxDepositParser implements DepositParser {
    private static final Logger logger = LogManager.getLogger(SaxDepositParser.class);
    private final SAXParser saxParser;

    public SaxDepositParser() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            saxParser = factory.newSAXParser();
        } catch (Exception e) {
            logger.error("Error creating SAXParser", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Deposit> parse(String filePath) {
        DepositHandler handler = new DepositHandler();
        try {
            saxParser.parse(filePath, handler);
            logger.info("SAX Parser successfully parsed {} deposits.", handler.getDeposits().size());
        } catch (Exception e) {
            logger.error("SAX Parsing error", e);
        }
        return handler.getDeposits();
    }

    // внутрішній клас-обробник для SAX
    private static class DepositHandler extends DefaultHandler {
        private final List<Deposit> deposits = new ArrayList<>();
        private Deposit currentDeposit;
        private BankXmlTag currentTag;

        public List<Deposit> getDeposits() { return deposits; }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if (qName.equals(BankXmlTag.DEPOSIT.getValue())) {
                currentDeposit = new Deposit();
                currentDeposit.setAccountId(attributes.getValue(BankXmlTag.ACCOUNT_ID.getValue()));
            } else if (currentDeposit != null) {
                try {
                    currentTag = BankXmlTag.fromValue(qName);
                } catch (IllegalArgumentException e) {
                    currentTag = null; // ігноруємо невідомі теги (наприклад кореневий Bank)
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            if (currentTag != null && currentDeposit != null) {
                String data = new String(ch, start, length).trim();
                if (data.isEmpty()) return;

                switch (currentTag) {
                    case NAME -> currentDeposit.setName(data);
                    case COUNTRY -> currentDeposit.setCountry(data);
                    case TYPE -> currentDeposit.setType(DepositType.fromValue(data));
                    case DEPOSITOR -> currentDeposit.setDepositor(data);
                    case AMOUNT_ON_DEPOSIT -> currentDeposit.setAmountOnDeposit(Double.parseDouble(data));
                    case PROFITABILITY -> currentDeposit.setProfitability(Double.parseDouble(data));
                    case TIME_CONSTRAINTS -> currentDeposit.setTimeConstraints(Integer.parseInt(data));
                    default -> {}
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if (qName.equals(BankXmlTag.DEPOSIT.getValue())) {
                deposits.add(currentDeposit);
                currentDeposit = null;
            }
            currentTag = null;
        }
    }
}