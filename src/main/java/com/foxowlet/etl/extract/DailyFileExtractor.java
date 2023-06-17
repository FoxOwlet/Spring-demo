package com.foxowlet.etl.extract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Component
@Profile("fruitShop")
public class DailyFileExtractor implements Extractor<String> {
    private final DateTimeFormatter pattern;
    private final Clock clock;

    @Autowired
    public DailyFileExtractor(@Value("${pattern}") String pattern, Clock clock) {
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
