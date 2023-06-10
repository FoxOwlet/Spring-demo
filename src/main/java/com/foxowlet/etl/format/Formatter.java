package com.foxowlet.etl.format;

public interface Formatter<T> {
    String format(T entity);
}
