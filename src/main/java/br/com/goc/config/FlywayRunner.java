package br.com.goc.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Order(0)
public class FlywayRunner implements CommandLineRunner {
    private final Flyway flyway;
    private final DataSource dataSource;
    private final Logger log = LoggerFactory.getLogger(FlywayRunner.class);

    public FlywayRunner(Flyway flyway, DataSource dataSource) {
        this.flyway = flyway;
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("FlywayRunner: starting migration...");
        try {
            MigrateResult result = flyway.migrate();
            log.info("FlywayRunner: migration result = {}", result);
            return;
        } catch (Exception e) {
            log.warn("FlywayRunner: Flyway migration failed (will attempt fallback SQL execution): {}", e.getMessage());
        }

        // Fallback: execute SQL scripts under classpath:db/migration
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:db/migration/*.sql");
            if (resources == null || resources.length == 0) {
                log.warn("FlywayRunner: no SQL migration resources found for fallback");
                return;
            }
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            // ensure scripts run in alphabetical order
            java.util.Arrays.sort(resources, (a, b) -> a.getFilename().compareTo(b.getFilename()));
            for (Resource r : resources) {
                log.info("FlywayRunner: adding fallback script {}", r.getFilename());
                populator.addScript(r);
            }
            DatabasePopulatorUtils.execute(populator, dataSource);
            log.info("FlywayRunner: fallback SQL scripts executed successfully");
        } catch (Exception ex) {
            log.error("FlywayRunner: fallback SQL execution failed: {}", ex.getMessage(), ex);
            throw ex;
        }
    }
}
