package org.russellrc.playground;

import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DropKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;

@Configuration
public class CassandraConfiguration extends AbstractCassandraConfiguration {

    private final CassandraProperties cassandraProperties;

    @Inject
    public CassandraConfiguration(final CassandraProperties cassandraProperties) {
        this.cassandraProperties = cassandraProperties;
    }

    @Override
    protected String getKeyspaceName() {
        return cassandraProperties.getKeyspaceName();
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        final CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace(cassandraProperties.getKeyspaceName())
            .with(KeyspaceOption.DURABLE_WRITES, true)
            .withSimpleReplication();
        return List.of(specification);
    }

    @Override
    protected List<DropKeyspaceSpecification> getKeyspaceDrops() {
        return List.of(DropKeyspaceSpecification.dropKeyspace(cassandraProperties.getKeyspaceName()));
    }

    @Override
    public SchemaAction getSchemaAction() {
        return cassandraProperties.getSchemaAction() == null
            ? super.getSchemaAction()
            : SchemaAction.valueOf(cassandraProperties.getSchemaAction().toUpperCase(Locale.ROOT));
    }

    @Override
    protected String getContactPoints() {
        return String.join(",", cassandraProperties.getContactPoints());
    }
}
