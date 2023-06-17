package com.foxowlet.etl.config;

import com.foxowlet.etl.domain.CustomerSummary;
import com.foxowlet.etl.format.Formatter;
import com.foxowlet.etl.load.FileLoader;
import com.foxowlet.etl.load.FormattedLoader;
import com.foxowlet.etl.load.Loader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import java.util.Objects;

@Configuration
@Profile("fruitShop")
@PropertySource("classpath:/fruit-shop.properties")
public class FruitShopConfiguration {

    @Bean
    public FileLoader csvLoader(@Value("${filename.csv}") String filename) {
        return new FileLoader(filename);
    }

    @Bean
    public FileLoader txtLoader(@Value("${filename.txt}") String filename) {
        return new FileLoader(filename);
    }

    @Bean
    @Qualifier("summaryLoader")
    public FormattedLoader<CustomerSummary> prodLoader(
            @Qualifier("csvLoader") Loader<String> loader,
            Formatter<CustomerSummary> formatter) {
        return new FormattedLoader<>(loader, formatter);
    }

    @Bean
    @Qualifier("summaryLoader")
    public FormattedLoader<CustomerSummary> devLoader(
            @Qualifier("txtLoader") Loader<String> loader) {
        return new FormattedLoader<>(loader, Objects::toString);
    }
}
