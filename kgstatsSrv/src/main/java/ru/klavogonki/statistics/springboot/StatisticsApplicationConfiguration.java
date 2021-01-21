package ru.klavogonki.statistics.springboot;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Import({StatisticsApplicationConfiguration.DatabaseConfiguration.class, StatisticsApplicationConfiguration.NoDatabaseConfiguration.class})
public class StatisticsApplicationConfiguration { // see https://stackoverflow.com/a/55591954/8534088

    @Profile(Profiles.DATABASE)
    @EnableJpaRepositories("ru.klavogonki.statistics")
    @EnableAutoConfiguration
    static class DatabaseConfiguration {
    }

    @Profile(Profiles.NO_DATABASE)
    @EnableAutoConfiguration(exclude = { // disable JPA, will work without the database
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
    })
    static class NoDatabaseConfiguration {
    }
}
