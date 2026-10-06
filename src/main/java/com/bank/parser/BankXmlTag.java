package com.bank.parser;

public enum BankXmlTag {
    BANK("Bank"),
    DEPOSIT("Deposit"),
    ACCOUNT_ID("accountId"), // атрибут
    NAME("Name"),
    COUNTRY("Country"),
    TYPE("Type"),
    DEPOSITOR("Depositor"),
    AMOUNT_ON_DEPOSIT("AmountOnDeposit"),
    PROFITABILITY("Profitability"),
    TIME_CONSTRAINTS("TimeConstraints");

    private final String value;

    BankXmlTag(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static BankXmlTag fromValue(String value) {
        for (BankXmlTag tag : BankXmlTag.values()) {
            if (tag.getValue().equals(value)) {
                return tag;
            }
        }
        throw new IllegalArgumentException("Unknown tag: " + value);
    }
}