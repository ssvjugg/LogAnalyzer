package ru.usernamedrew.implementation;

import ru.usernamedrew.api.*;
import ru.usernamedrew.exeptions.*;
import ru.usernamedrew.implementation.operations.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.regex.Matcher;

public class LogParser implements Parser {
    @Override
    public Event parse(String line) throws NegativeAmountException, UnknownOperationException {
        Matcher matcher = logPattern.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid log format: " + line);
        }

        LocalDateTime dateTime = LocalDateTime.parse(matcher.group(1), formatter);
        String user = matcher.group(2);
        String operationName = matcher.group(3);
        BigDecimal amount = new BigDecimal(matcher.group(4));

        // This is hardcoded parse of operation but can be replaced by map of allowed operations (Also pattern should be modified)
        Operation operation = switch(operationName) {
            case "balance inquiry" -> new BalanceInquiry(amount);
            case "withdrew" -> new Withdrawal(amount);
            case "transferred" -> {
                if (matcher.group(5) == null) {
                    throw new UnknownOperationException("Unknown operation: " + operationName + ", missing the amount of inquiry");
                }
                yield new Transfer(amount, matcher.group(5));
            }
            default -> {
                throw new UnknownOperationException("Unknown operation: " + operationName);
            }
        };

        return new Event(dateTime, operation, user);
    }
}
