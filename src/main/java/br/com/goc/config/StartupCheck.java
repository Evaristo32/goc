package br.com.goc.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class StartupCheck implements CommandLineRunner {
    private final JdbcTemplate jdbc;
    private final Logger log = LoggerFactory.getLogger(StartupCheck.class);

    public StartupCheck(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            Integer clienteCount = jdbc.queryForObject("SELECT count(*) FROM cliente", Integer.class);
            log.info("StartupCheck: cliente table exists, count = {}", clienteCount);
        } catch (Exception e) {
            log.warn("StartupCheck: could not query cliente table: {}", e.getMessage());
        }

        try {
            Integer flywayCount = jdbc.queryForObject("SELECT count(*) FROM flyway_schema_history", Integer.class);
            log.info("StartupCheck: flyway_schema_history exists, migrations = {}", flywayCount);
        } catch (Exception e) {
            log.warn("StartupCheck: could not query flyway_schema_history: {}", e.getMessage());
        }
    }
}

