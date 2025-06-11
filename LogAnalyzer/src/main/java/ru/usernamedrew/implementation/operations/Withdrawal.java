package ru.usernamedrew.implementation.operations;

import ru.usernamedrew.exeptions.NegativeAmountException;
import ru.usernamedrew.implementation.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Withdrawal extends Transaction {
    public Withdrawal(BigDecimal amount) throws NegativeAmountException {
        super(amount);
    }

    @Override
    public String toString() {
        return "withdrew " + getAmount();
    }
}
