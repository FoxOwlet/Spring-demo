package com.foxowlet.etl.load;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.foxowlet.etl.util.TestUtils.assertStreamContent;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MultiLoaderTest {
    @Captor
    private ArgumentCaptor<Stream<String>> captor;

    @Test
    void load_shouldDelegateToAllChildren() {
        List<Loader<String>> mocks = makeLoaders(3);
        Loader<String> loader = new MultiLoader<>(mocks);

        loader.load(Stream.empty());

        for (Loader<String> mock : mocks) {
            Mockito.verify(mock).load(Mockito.any());
        }
    }

    @Test
    void load_shouldDelegateSameContentToAllChildren() {
        String[] lines = {"foo", "bar", "buz"};
        List<Loader<String>> mocks = makeLoaders(5);
        Loader<String> loader = new MultiLoader<>(mocks);

        loader.load(Arrays.stream(lines));

        assertEquals(mocks.size(), captor.getAllValues().size());
        for (Stream<String> actual : captor.getAllValues()) {
            assertStreamContent(actual, lines);
        }
    }

    private List<Loader<String>> makeLoaders(int amount) {
        return Stream.generate(this::makeLoader).limit(amount).toList();
    }

    private Loader<String> makeLoader() {
        Loader<String> mock = Mockito.mock(Loader.class);
        Mockito.doNothing().when(mock).load(captor.capture());
        return mock;

    }
}