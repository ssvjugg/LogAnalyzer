package ru.usernamedrew.api;

import ru.usernamedrew.implementation.Event;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public interface Parser {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Pattern logPatter = Pattern.compile("^\\[(.*?)\\] (\\S+) (balance inquiry|transferred|withdrew) (\\d+(?:\\.\\d+)?)(?: to (\\S+))?$");

    Event parse(String line);
}
