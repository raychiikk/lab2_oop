package com.bank.model;

import java.util.Objects;

public class Deposit implements Comparable<Deposit> {
    private String accountId;
    private String name;
    private String country;
    private DepositType type;
    private String depositor;
    private double amountOnDeposit;
    private double profitability;
    private int timeConstraints; // в місяцях

    // порожній конструктор
    public Deposit() {}

    // геттери та Сеттери
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public DepositType getType() { return type; }
    public void setType(DepositType type) { this.type = type; }

    public String getDepositor() { return depositor; }
    public void setDepositor(String depositor) { this.depositor = depositor; }

    public double getAmountOnDeposit() { return amountOnDeposit; }
    public void setAmountOnDeposit(double amountOnDeposit) { this.amountOnDeposit = amountOnDeposit; }

    public double getProfitability() { return profitability; }
    public void setProfitability(double profitability) { this.profitability = profitability; }

    public int getTimeConstraints() { return timeConstraints; }
    public void setTimeConstraints(int timeConstraints) { this.timeConstraints = timeConstraints; }

    // перевизначаємо equals та hashCode для тестів та коректного порівняння об'єктів
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deposit deposit = (Deposit) o;
        return Objects.equals(accountId, deposit.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    @Override
    public String toString() {
        return String.format("Deposit{id='%s', name='%s', type=%s, depositor='%s', amount=%.2f, profit=%.1f%%, months=%d}",
                accountId, name, type.getValue(), depositor, amountOnDeposit, profitability, timeConstraints);
    }

    // дефолтне сортування (за ID рахунку)
    @Override
    public int compareTo(Deposit o) {
        return this.accountId.compareTo(o.getAccountId());
    }
}