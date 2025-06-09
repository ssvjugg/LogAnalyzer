package ru.usernamedrew.implementation;

import ru.usernamedrew.api.Operation;
import ru.usernamedrew.api.Parser;
import ru.usernamedrew.implementation.operations.BalanceInquiry;
import ru.usernamedrew.implementation.operations.Transfer;
import ru.usernamedrew.implementation.operations.Withdrawal;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.regex.Matcher;

public class LogParser implements Parser {
    @Override
    public Event parse(String line) {
        Matcher matcher = logPatter.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid log format: " + line);
        }

        LocalDateTime dateTime = LocalDateTime.parse(matcher.group(1), formatter);
        String user = matcher.group(2);
        String operationName = matcher.group(3);
        BigDecimal amount = new BigDecimal(matcher.group(4));

        Operation operation = switch(operationName) {
            case "balance inquiry" -> new BalanceInquiry(amount);
            case "withdrew" -> new Withdrawal(amount);
            case "transferred" -> new Transfer(amount, matcher.group(5));
            default -> throw new IllegalArgumentException("Unknown operation: " + operationName);
        };

        return new Event(dateTime, operation, user);
    }
}
