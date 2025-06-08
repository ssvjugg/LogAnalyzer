package ru.usernamedrew.implementation.operations;

import ru.usernamedrew.implementation.Transaction;

import java.time.LocalDateTime;

public class Transfer extends Transaction {
    private final String sender;
    private final String recipient;

    public Transfer(double amount, String recipient, String sender, LocalDateTime createdAt) {
        super(amount, createdAt);
        if ((recipient == null || recipient.isEmpty()) && (sender == null || sender.isEmpty())) {
            throw new IllegalArgumentException("Recipient and sender are empty");
        }
        this.recipient = recipient;
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }
}
