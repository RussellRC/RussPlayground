package org.russellrc.playground.domain;

import com.google.common.base.Preconditions;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

@PrimaryKeyClass
public class BulkOperationKey {

    @PrimaryKeyColumn(name = "owner_datacenter", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String ownerDatacenter;

    @PrimaryKeyColumn(name = "owner_tenant", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String ownerTenant;

    @PrimaryKeyColumn(name = "bulk_operation_uuid", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    @CassandraType(type = CassandraType.Name.TIMEUUID)
    private UUID uuid;

    @SuppressWarnings("unused")
    private BulkOperationKey() {}

    public BulkOperationKey(@Nonnull final String ownerDatacenter, @Nonnull final String ownerTenant, @Nonnull final UUID uuid) {
        Preconditions.checkArgument(uuid.version() == 1, "UUID has to be version 1");
        this.ownerDatacenter = ownerDatacenter;
        this.ownerTenant = ownerTenant;
        this.uuid = uuid;
    }

    public String getOwnerDatacenter() {
        return ownerDatacenter;
    }

    public String getOwnerTenant() {
        return ownerTenant;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BulkOperationKey that = (BulkOperationKey) o;
        return Objects.equals(getOwnerDatacenter(), that.getOwnerDatacenter()) &&
            Objects.equals(getOwnerTenant(), that.getOwnerTenant()) &&
            Objects.equals(getUuid(), that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerDatacenter, ownerTenant, uuid);
    }

    @Override
    public String toString() {
        return "BulkOperationKey{" +
            "ownerDatacenter='" + ownerDatacenter + '\'' +
            ", ownerTenant='" + ownerTenant + '\'' +
            ", uuid=" + uuid +
            '}';
    }
}
