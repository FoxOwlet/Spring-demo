package com.foxowlet.etl.parser;

public interface Parser<T> {
    T parse(String input);
}
