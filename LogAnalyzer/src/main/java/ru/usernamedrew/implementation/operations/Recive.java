package ru.usernamedrew.implementation.operations;

import ru.usernamedrew.implementation.Transaction;

public class Recive extends Transaction {
    private final String sender;

    public Recive(double amount, String sender) {
        super(amount);
        if (sender == null || sender.isEmpty()) {
            throw new IllegalArgumentException("Sender is empty");
        }
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }
}
