package com.foxowlet.etl.extract;

import com.foxowlet.etl.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@Primary
public class ParsedExtractor<T> implements Extractor<T> {
    private final Extractor<String> rawExtractor;
    private final Parser<T> parser;

    @Autowired
    public ParsedExtractor(Extractor<String> rawExtractor, Parser<T> parser) {
        this.rawExtractor = rawExtractor;
        this.parser = parser;
    }

    @Override
    public Stream<T> extract() {
        return rawExtractor.extract().map(parser::parse);
    }
}
