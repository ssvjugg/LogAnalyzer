package ru.usernamedrew.implementation.operations;

import ru.usernamedrew.implementation.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Withdrawal extends Transaction {
    public Withdrawal(BigDecimal amount) {
        super(amount);
    }

    @Override
    public String toString() {
        return "withdrew " + getAmount();
    }
}
