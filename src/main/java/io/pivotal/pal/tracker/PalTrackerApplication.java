package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class PalTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PalTrackerApplication.class, args);
    }

    @Bean
    public TimeEntryRepository getTimeEntryRepository(DataSource dataSource){
        return new JdbcTimeEntryRepository(dataSource);
    }

    @Bean
    public TimeEntryInfoContributor getTimeEntryInfoContributor(){
        return new TimeEntryInfoContributor();
    }

}
