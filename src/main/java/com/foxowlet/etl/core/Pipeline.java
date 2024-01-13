package com.foxowlet.etl.core;

import com.foxowlet.etl.extract.Extractor;
import com.foxowlet.etl.load.Loader;
import com.foxowlet.etl.transform.Transformation;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class Pipeline<T, R> {
    private final Extractor<T> extractor;
    private final Transformation<T, R> transformation;
    private final Loader<R> loader;

    public Pipeline(Extractor<T> extractor, Transformation<T, R> transformation, Loader<R> loader) {
        this.extractor = extractor;
        this.transformation = transformation;
        this.loader = loader;
    }

    @PostConstruct
    public void run() {
        Stream<T> raw = extractor.extract();
        Stream<R> transformed = transformation.transform(raw);
        loader.load(transformed);
    }
}
