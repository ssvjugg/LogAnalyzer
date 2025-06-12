package ru.usernamedrew.implementation;

import org.junit.jupiter.api.Test;
import ru.usernamedrew.api.Parser;
import ru.usernamedrew.exeptions.*;
import ru.usernamedrew.implementation.operations.*;

import static org.junit.jupiter.api.Assertions.*;

class LogParserTest {
    Parser parser = new LogParser();

    @Test
    void parseTransfer() throws NegativeAmountException, UnknownOperationException {
        Event event = parser.parse("[2025-05-10 09:05:44] user001 transferred 100.00 to user002");

        assertEquals("user001", event.getUser());
        assertInstanceOf(Transfer.class, event.getOperation());
    }

    @Test
    void parseBalanceInquiry() throws NegativeAmountException, UnknownOperationException {
        Event event = parser.parse("[2025-05-10 09:00:22] user001 balance inquiry 1000.00");

        assertEquals("user001", event.getUser());
        assertInstanceOf(BalanceInquiry.class, event.getOperation());
    }

    @Test
    void parseWithdrawal() throws NegativeAmountException, UnknownOperationException {
        Event event = parser.parse("[2025-05-10 23:55:32] user002 withdrew 50");

        assertEquals("user002", event.getUser());
        assertInstanceOf(Withdrawal.class, event.getOperation());
    }

    @Test
    void parseNegativeAmountException()  {
        assertThrows(NegativeAmountException.class, () -> {parser.parse("[2025-05-10 23:55:32] user002 withdrew -50");});
    }

    @Test
    void parseInvalidFormatException()  {
        assertThrows(IllegalArgumentException.class, () -> {parser.parse("[2025-05-10 23:55:32]");});
    }
}