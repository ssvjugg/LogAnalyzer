package ru.usernamedrew.implementation.operations;

import ru.usernamedrew.exeptions.NegativeAmountException;
import ru.usernamedrew.implementation.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BalanceInquiry extends Transaction {
    public BalanceInquiry(BigDecimal amount) throws NegativeAmountException {
        super(amount);
    }

    @Override
    public String toString() {
        return "balance inquiry " + getAmount();
    }
}
