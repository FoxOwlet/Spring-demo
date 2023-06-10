package com.foxowlet.etl.extract;

import java.util.stream.Stream;

public interface Extractor<T> {
    Stream<T> extract();
}
