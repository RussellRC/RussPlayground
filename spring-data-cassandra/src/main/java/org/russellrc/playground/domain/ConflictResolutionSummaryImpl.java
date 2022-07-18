package org.russellrc.playground.domain;

import org.springframework.data.annotation.Version;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;

@Table("conflict_resolutions_summary")
public class ConflictResolutionSummaryImpl implements ConflictResolutionSummary {

    @PrimaryKey
    private BulkOperationKey key;

    @Column("conflict_resolutions")
    @CassandraType(type = CassandraType.Name.SET, typeArguments = CassandraType.Name.UDT, userTypeName = "conflict_resolution")
    private Set<ConflictResolution> conflictResolutions;

    @Version
    @Column("opt_lock")
    public long optLock;

    /** Constructor needed by spring-cassandra */
    @SuppressWarnings("unused")
    public ConflictResolutionSummaryImpl() {}

    /**
     * Constructor
     */
    public ConflictResolutionSummaryImpl(@Nonnull final String ownerDatacenter, @Nonnull final String ownerTenant, @Nonnull final UUID bulkOperationUuid,
        @Nonnull final Set<ConflictResolution> conflictResolutions,  final long optLock) {

        this.key = new BulkOperationKey(ownerDatacenter, ownerTenant, bulkOperationUuid);
        this.conflictResolutions = Set.copyOf(conflictResolutions);
        this.optLock = optLock;
    }

    @Override
    public String getOwnerDatacenter() {
        return key.getOwnerDatacenter();
    }

    @Override
    public String getOwnerTenant() {
        return key.getOwnerTenant();
    }

    @Override
    public UUID getBulkOperationUuid() {
        return key.getUuid();
    }

    @Override
    public Set<ConflictResolution> getConflictResolutions() {
        return conflictResolutions;
    }

    public long getOptLock() {
        return optLock;
    }

    @Override
    public Builder mutate() {
        return new Builder(this);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConflictResolutionSummaryImpl)) {
            return false;
        }
        final ConflictResolutionSummaryImpl that = (ConflictResolutionSummaryImpl) o;
        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

}
