package com.foxowlet.etl.extract;

import com.foxowlet.etl.parser.Parser;

import java.util.stream.Stream;

public class ParsedExtractor<T> implements Extractor<T> {
    private final Extractor<String> rawExtractor;
    private final Parser<T> parser;

    public ParsedExtractor(Extractor<String> rawExtractor, Parser<T> parser) {
        this.rawExtractor = rawExtractor;
        this.parser = parser;
    }

    @Override
    public Stream<T> extract() {
        return rawExtractor.extract().map(parser::parse);
    }
}
