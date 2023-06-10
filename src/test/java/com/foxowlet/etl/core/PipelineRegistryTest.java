package com.foxowlet.etl.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PipelineRegistryTest {
    @Mock
    private Pipeline<String, Integer> pipeline;
    private PipelineRegistry<String, String, Integer> registry = new PipelineRegistry<>();

    @Test
    void resolve_shouldReturnEmptyOptional_whenNoPipelineRegistered() {
        assertTrue(registry.resolve("foo").isEmpty());
    }

    @Test
    void resolve_shouldReturnRegisteredPipeline() {
        registry.register("foo", pipeline);

        Optional<Pipeline<String, Integer>> actual = registry.resolve("foo");
        assertTrue(actual.isPresent());
        assertSame(pipeline, actual.get());
    }
}