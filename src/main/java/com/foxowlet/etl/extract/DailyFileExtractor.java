package com.foxowlet.etl.extract;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class DailyFileExtractor implements Extractor<String> {
    private final DateTimeFormatter pattern;
    private final Clock clock;

    public DailyFileExtractor(String pattern, Clock clock) {
        this(DateTimeFormatter.ofPattern(pattern), clock);
    }

    public DailyFileExtractor(DateTimeFormatter pattern, Clock clock) {
        this.pattern = pattern;
        this.clock = clock;
    }

    @Override
    public Stream<String> extract() {
        LocalDate currentDate = LocalDate.now(clock);
        String fileName = pattern.format(currentDate);
        return new FileExtractor(fileName).extract();
    }
}
