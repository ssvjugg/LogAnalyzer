package ru.usernamedrew.implementation.operations;

import ru.usernamedrew.implementation.Transaction;

import java.time.LocalDateTime;

public class Transfer extends Transaction {
    private final String recipient;

    public Transfer(double amount, String recipient) {
        super(amount);
        if (recipient == null || recipient.isEmpty()) {
            throw new IllegalArgumentException("Recipient is empty");
        }
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }
}
