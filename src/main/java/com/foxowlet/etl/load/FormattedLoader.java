package com.foxowlet.etl.load;

import com.foxowlet.etl.format.Formatter;

import java.util.stream.Stream;

public class FormattedLoader<T> implements Loader<T> {
    private final Loader<String> loader;
    private final Formatter<T> formatter;

    public FormattedLoader(Loader<String> loader, Formatter<T> formatter) {
        this.loader = loader;
        this.formatter = formatter;
    }

    @Override
    public void load(Stream<T> data) {
        loader.load(data.map(formatter::format));
    }
}
