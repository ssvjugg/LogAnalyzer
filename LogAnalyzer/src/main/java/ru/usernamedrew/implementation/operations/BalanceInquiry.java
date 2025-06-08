package ru.usernamedrew.implementation.operations;

import ru.usernamedrew.implementation.Transaction;

import java.time.LocalDateTime;

public class BalanceInquiry extends Transaction {
    public BalanceInquiry(double amount, LocalDateTime createdAt) {
        super(amount, createdAt);
    }
}
