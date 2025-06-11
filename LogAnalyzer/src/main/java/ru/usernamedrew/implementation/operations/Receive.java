package ru.usernamedrew.implementation.operations;

import ru.usernamedrew.api.Operation;

import java.math.BigDecimal;

public final class Receive implements Operation {
    private final BigDecimal amount;
    private final String sender;

    public Receive(BigDecimal amount, String sender) {
        if (sender == null || sender.isEmpty()) {
            throw new IllegalArgumentException("Sender is empty");
        }
        this.amount = amount;
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("received  %s from %s", amount, sender);
    }
}
