package com.bank.parser;

import com.bank.model.Deposit;
import java.util.List;

public interface DepositParser {
    List<Deposit> parse(String filePath);
}