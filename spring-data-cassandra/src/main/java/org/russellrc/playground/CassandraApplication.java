package org.russellrc.playground;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import org.russellrc.playground.domain.ConflictResolution;
import org.russellrc.playground.domain.ConflictResolutionOperation;
import org.russellrc.playground.domain.ConflictResolutionSummary;
import org.russellrc.playground.domain.ConflictResolutionSummaryImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.CassandraTemplate;

import javax.inject.Inject;
import java.util.Set;
import java.util.UUID;

@Configuration
@EntityScan(basePackages = {"org.russellrc.playground.domain"})
@SpringBootApplication
public class CassandraApplication implements CommandLineRunner {

    final CassandraTemplate template;

    @Inject
    public CassandraApplication(final CassandraTemplate template) {
        this.template = template;
    }

    public static void main(String[] args) {
        SpringApplication.run(CassandraApplication.class, args);
    }

    @Override
    public void run(final String... args) throws Exception {

        final ConflictResolution cr1 = new ConflictResolution(UUID.randomUUID(), ConflictResolutionOperation.APPLY);
        final ConflictResolutionSummaryImpl summary = ConflictResolutionSummary.builder("sea1", "apps", Uuids.timeBased())
            .setConflictResolutions(Set.of(cr1))
            .build();

        final ConflictResolutionSummaryImpl inserted = template.insert(summary);
        System.out.println(inserted.getOptLock());

        final ConflictResolution cr2 = new ConflictResolution(UUID.randomUUID(), ConflictResolutionOperation.APPLY);

        final ConflictResolutionSummaryImpl mutated1 = inserted.mutate()
            .setConflictResolutions(Set.of(cr1, cr2))
            .build();

        final ConflictResolutionSummaryImpl updated1 = template.update(mutated1);
        System.out.println(updated1.getOptLock());

        updated1.optLock = 1;
        final ConflictResolutionSummaryImpl updated2 = template.update(updated1);
        System.out.println(updated2.getOptLock());
    }
}
