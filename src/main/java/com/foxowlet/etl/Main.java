package com.foxowlet.etl;


import com.foxowlet.etl.core.Pipeline;
import com.foxowlet.etl.core.PipelineRegistry;
import com.foxowlet.etl.domain.CustomerSummary;
import com.foxowlet.etl.domain.Product;
import com.foxowlet.etl.extract.DailyFileExtractor;
import com.foxowlet.etl.extract.FileExtractor;
import com.foxowlet.etl.extract.ParsedExtractor;
import com.foxowlet.etl.format.CustomerSummaryCSVFormatter;
import com.foxowlet.etl.format.CustomerSummaryJSONFormatter;
import com.foxowlet.etl.load.FileLoader;
import com.foxowlet.etl.load.FormattedLoader;
import com.foxowlet.etl.load.JdbcLoader;
import com.foxowlet.etl.load.MultiLoader;
import com.foxowlet.etl.parser.ProductCSVParser;
import com.foxowlet.etl.transform.SummaryAggregation;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class Main {
    private static final LocalDate TEST_DATE = LocalDate.of(2023, 6, 9);
    private static final String DEFAULT_KEY = "fruitShop";
    private static final PipelineRegistry<String, Product, CustomerSummary> registry = new PipelineRegistry<>();

    static {
        Clock clock = makeClock();
        Pipeline<Product, CustomerSummary> fruitShopPipeline = new Pipeline<>(
                new ParsedExtractor<>(
                        new DailyFileExtractor("'data/fruits-'YYYY-MM-dd'.csv'", clock),
                        new ProductCSVParser()),
                new SummaryAggregation(clock),
                new MultiLoader<>(List.of(
                        new FormattedLoader<>(
                                new FileLoader("data/fruits-latest-summary.csv"),
                                new CustomerSummaryCSVFormatter()),
                        new FormattedLoader<>(
                                new FileLoader("data/fruits-test-summary.txt"),
                                Object::toString))));

        Pipeline<Product, CustomerSummary> flowerShopPipeline = new Pipeline<>(
                new ParsedExtractor<>(
                        new FileExtractor("data/flower-latest.csv"),
                        new ProductCSVParser()),
                new SummaryAggregation(clock),
                new MultiLoader<>(List.of(
                        new FormattedLoader<>(
                                new FileLoader("data/flower-latest.jsonl"),
                                new CustomerSummaryJSONFormatter()),
                        new JdbcLoader<>(
                                new HikariDataSource(new HikariConfig("/datasource.properties")),
                                "customer_summary",
                                CustomerSummary.class))));


        registry.register("fruitShop", fruitShopPipeline);
        registry.register("flowerShop", flowerShopPipeline);
    }

    public static void main(String[] args) {
        registry.resolve(getShopKey(args)).ifPresent(Pipeline::run);
    }

    private static Clock makeClock() {
        ZoneId zone = ZoneId.systemDefault();
        return Clock.fixed(TEST_DATE.atStartOfDay(zone).toInstant(), zone);
    }

    private static String getShopKey(String[] args) {
        return args.length == 0 ? DEFAULT_KEY : args[0];
    }
}
