package ru.usernamedrew.exeptions;

import ru.usernamedrew.api.Operation;

public class UnknownOperationException extends Exception {
    public UnknownOperationException(String operationName) {
        super("Unknown operation: " + operationName);
    }
}
