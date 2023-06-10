package com.foxowlet.etl.load;

import java.util.stream.Stream;

public interface Loader<T> {
    void load(Stream<T> data);
}
