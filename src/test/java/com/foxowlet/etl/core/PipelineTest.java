package com.foxowlet.etl.core;

import com.foxowlet.etl.extract.Extractor;
import com.foxowlet.etl.load.Loader;
import com.foxowlet.etl.transform.Transformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.stream.Stream;

import static com.foxowlet.etl.util.TestUtils.*;

@ExtendWith(MockitoExtension.class)
class PipelineTest {
    @Mock
    private Extractor<String> extractor;
    @Mock
    private Transformation<String, Integer> transformation;
    @Mock
    private Loader<Integer> loader;
    @InjectMocks
    private Pipeline<String, Integer> pipeline;
    @Captor
    private ArgumentCaptor<Stream<String>> inputCaptor;
    @Captor
    private ArgumentCaptor<Stream<Integer>> outputCaptor;

    @Test
    void run_shouldPipeData() {
        initExtractor("a", "b", "c");
        initTransformation(1, 2, 3, 4, 5);
        initLoader();

        pipeline.run();

        assertStreamContent(inputCaptor.getValue(), "a", "b", "c");
        assertStreamContent(outputCaptor.getValue(), 1, 2, 3, 4, 5);
    }

    private void initExtractor(String... content) {
        Mockito.when(extractor.extract())
                .thenReturn(Arrays.stream(content));
    }

    private void initTransformation(int... content) {
        Mockito.when(transformation.transform(inputCaptor.capture()))
                .thenReturn(Arrays.stream(content).boxed());
    }

    private void initLoader() {
        Mockito.doNothing().when(loader).load(outputCaptor.capture());
    }
}