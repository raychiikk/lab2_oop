package com.bank.model;

public enum DepositType {
    DEMAND("до запитання"),
    TERM("строковий"),
    SETTLEMENT("розрахунковий"),
    ACCUMULATIVE("накопичувальний"),
    SAVINGS("ощадний"),
    METAL("металевий");

    private final String value;

    DepositType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DepositType fromValue(String value) {
        for (DepositType type : DepositType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown deposit type: " + value);
    }
}
