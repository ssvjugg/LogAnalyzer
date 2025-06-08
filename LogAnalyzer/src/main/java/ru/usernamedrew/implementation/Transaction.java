package ru.usernamedrew.implementation;

import ru.usernamedrew.api.Operation;

import java.time.LocalDateTime;

public abstract class Transaction implements Operation {
    private final double amount;
    private final LocalDateTime createdAt;

    public Transaction(double amount, LocalDateTime createdAt) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
