package ru.usernamedrew.implementation.operations;

import ru.usernamedrew.implementation.Transaction;

import java.time.LocalDateTime;

public class Withdrawal extends Transaction {
    public Withdrawal(double amount, LocalDateTime createdAt) {
        super(amount, createdAt);
    }
}
