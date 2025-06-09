package ru.usernamedrew.implementation;

import ru.usernamedrew.api.Operation;
import ru.usernamedrew.api.Parser;

import java.time.LocalDateTime;

public class Event {
    private final LocalDateTime createdAt;
    private final Operation operation;
    private final String user;

    public Event(LocalDateTime createdAt, Operation operation, String user) {
        this.createdAt = createdAt;
        this.operation = operation;
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Operation getOperation() {
        return operation;
    }

    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", Parser.formatter.format(createdAt), user, operation);
    }
}
