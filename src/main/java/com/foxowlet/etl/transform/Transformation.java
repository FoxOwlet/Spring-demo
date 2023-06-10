package com.foxowlet.etl.transform;

import java.util.stream.Stream;

public interface Transformation<T, R> {
    Stream<R> transform(Stream<T> data);

    default <R2> Transformation<T, R2> andThen(Transformation<R, R2> transformation) {
        return data -> transformation.transform(transform(data));
    }
}
