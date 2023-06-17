package com.foxowlet.etl.load;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
@Primary
public class MultiLoader<T> implements Loader<T> {
    private final List<Loader<T>> loaders;

    @Autowired
    public MultiLoader(@Qualifier("summaryLoader") List<Loader<T>> loaders) {
        this.loaders = loaders;
    }

    @Override
    public void load(Stream<T> data) {
        List<T> collected = data.toList();
        loaders.forEach(l -> l.load(collected.stream()));
    }
}
