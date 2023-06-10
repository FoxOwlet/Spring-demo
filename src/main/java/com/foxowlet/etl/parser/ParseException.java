package com.foxowlet.etl.parser;

public class ParseException extends RuntimeException {
    public ParseException(String entity, String reason, String input) {
        super(String.format("Can't parse %s: %s. Input: %s", entity, reason, input));
    }

    public ParseException(String entity, String input, Throwable e) {
        super(String.format("Can't parse %s (input: %s)", entity, input), e);
    }
}
