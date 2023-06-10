package com.foxowlet.etl.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PipelineRegistry<K, T, R> {
    private final Map<K, Pipeline<T, R>> registry = new HashMap<>();

    public void register(K key, Pipeline<T, R> pipeline) {
        registry.put(key, pipeline);
    }

    public Optional<Pipeline<T, R>> resolve(K key) {
        return Optional.ofNullable(registry.get(key));
    }
}
