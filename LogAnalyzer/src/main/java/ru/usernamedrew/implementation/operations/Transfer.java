package ru.usernamedrew.implementation.operations;

import ru.usernamedrew.exeptions.NegativeAmountException;
import ru.usernamedrew.implementation.Transaction;

import java.math.BigDecimal;

public class Transfer extends Transaction {
    private final String recipient;

    public Transfer(BigDecimal amount, String recipient) throws NegativeAmountException {
        super(amount);
        if (recipient == null || recipient.isEmpty()) {
            throw new IllegalArgumentException("Recipient is empty");
        }
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }

    @Override
    public String toString() {
        return String.format("transferred %s to %s", getAmount(), getRecipient());
    }
}
