package com.foxowlet.etl.load;

import java.util.List;
import java.util.stream.Stream;

public class MultiLoader<T> implements Loader<T> {
    private final List<Loader<T>> loaders;

    public MultiLoader(List<Loader<T>> loaders) {
        this.loaders = loaders;
    }

    @Override
    public void load(Stream<T> data) {
        List<T> collected = data.toList();
        loaders.forEach(l -> l.load(collected.stream()));
    }
}
