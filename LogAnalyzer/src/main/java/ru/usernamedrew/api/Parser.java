package ru.usernamedrew.api;

import ru.usernamedrew.exeptions.*;
import ru.usernamedrew.implementation.Event;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public interface Parser {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Pattern logPattern = Pattern.compile("^\\[(.*?)\\] (\\S+) (balance inquiry|transferred|withdrew) (-?\\d+(?:\\.\\d+)?)(?: to (\\S+))?$");

    /**
     * This methode is responsible for selecting an Event from a string.
     * @param line Text from log file that should be parsed
     * @return Event model that represents some event in application
     * @throws NegativeAmountException if the amount of operation was negative
     */
    Event parse(String line) throws NegativeAmountException, UnknownOperationException;
}
