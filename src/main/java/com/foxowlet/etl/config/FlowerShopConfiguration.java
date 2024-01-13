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

@Configuration
@Profile("flowerShop")
public class FlowerShopConfiguration {

    @Bean
    public FileLoader loader(@Value("${output-file}") String filename) {
        return new FileLoader(filename);
    }

    @Bean
    @Qualifier("summaryLoader")
    public FormattedLoader<CustomerSummary> prodLoader(
            Loader<String> loader,
            Formatter<CustomerSummary> formatter) {
        return new FormattedLoader<>(loader, formatter);
    }

    @Bean
    public Class<?> entityClass() {
        return CustomerSummary.class;
    }
}
