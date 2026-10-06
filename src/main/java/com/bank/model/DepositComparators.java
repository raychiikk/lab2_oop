package com.bank.model;

import java.util.Comparator;

public class DepositComparators {

    // сортування за сумою вкладу (за спаданням)
    public static final Comparator<Deposit> BY_AMOUNT_DESC = Comparator
            .comparingDouble(Deposit::getAmountOnDeposit).reversed();

    // сортування за прибутковістю (відсотком)
    public static final Comparator<Deposit> BY_PROFITABILITY = Comparator
            .comparingDouble(Deposit::getProfitability);

    // сортування за іменем банку
    public static final Comparator<Deposit> BY_BANK_NAME = Comparator
            .comparing(Deposit::getName);
}