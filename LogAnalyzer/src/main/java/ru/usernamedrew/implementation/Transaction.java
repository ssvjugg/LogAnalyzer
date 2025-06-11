package ru.usernamedrew.implementation;

import ru.usernamedrew.api.Operation;
import ru.usernamedrew.exeptions.NegativeAmountException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class Transaction implements Operation {
    private final BigDecimal amount;

    public Transaction(BigDecimal amount) throws NegativeAmountException {
        if (amount == null || amount.doubleValue() < 0) {
            throw new NegativeAmountException("Amount must be greater than zero");
        }
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
