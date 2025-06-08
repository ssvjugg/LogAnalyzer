package ru.usernamedrew.implementation;

import ru.usernamedrew.api.Operation;

import java.time.LocalDateTime;

public abstract class Transaction implements Operation {
    private final double amount;

    public Transaction(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
