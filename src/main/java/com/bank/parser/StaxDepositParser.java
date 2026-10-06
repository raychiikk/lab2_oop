package com.bank.parser;

import com.bank.model.Deposit;
import com.bank.model.DepositType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class StaxDepositParser implements DepositParser {
    private static final Logger logger = LogManager.getLogger(StaxDepositParser.class);
    private final XMLInputFactory inputFactory;

    public StaxDepositParser() {
        inputFactory = XMLInputFactory.newInstance();
    }

    @Override
    public List<Deposit> parse(String filePath) {
        List<Deposit> deposits = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            XMLStreamReader reader = inputFactory.createXMLStreamReader(inputStream);
            Deposit currentDeposit = null;
            BankXmlTag currentTag = null;

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT -> {
                        String qName = reader.getLocalName();
                        if (qName.equals(BankXmlTag.DEPOSIT.getValue())) {
                            currentDeposit = new Deposit();
                            currentDeposit.setAccountId(reader.getAttributeValue(null, BankXmlTag.ACCOUNT_ID.getValue()));
                        } else if (currentDeposit != null) {
                            try {
                                currentTag = BankXmlTag.fromValue(qName);
                            } catch (IllegalArgumentException e) {
                                currentTag = null;
                            }
                        }
                    }
                    case XMLStreamConstants.CHARACTERS -> {
                        String data = reader.getText().trim();
                        if (!data.isEmpty() && currentTag != null && currentDeposit != null) {
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
                    case XMLStreamConstants.END_ELEMENT -> {
                        if (reader.getLocalName().equals(BankXmlTag.DEPOSIT.getValue()) && currentDeposit != null) {
                            deposits.add(currentDeposit);
                            currentDeposit = null;
                        }
                        currentTag = null;
                    }
                }
            }
            logger.info("StAX Parser successfully parsed {} deposits.", deposits.size());
        } catch (Exception e) {
            logger.error("StAX Parsing error", e);
        }
        return deposits;
    }
}