package ru.usernamedrew.implementation.operations;

import ru.usernamedrew.implementation.Transaction;

import java.math.BigDecimal;

public class Receive extends Transaction {
    private final String sender;

    public Receive(BigDecimal amount, String sender) {
        super(amount);
        if (sender == null || sender.isEmpty()) {
            throw new IllegalArgumentException("Sender is empty");
        }
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return String.format("received  %s from %s", getAmount(), sender);
    }
}
