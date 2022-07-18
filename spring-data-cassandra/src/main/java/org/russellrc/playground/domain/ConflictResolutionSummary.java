package org.russellrc.playground.domain;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Summary of {@link EntityOperation}s and their respective {@link ConflictResolutionOperation}s
 * of a Package Installation
 */
public interface ConflictResolutionSummary {

    /** @return Datacenter of the Tenant who owns the Package */
    String getOwnerDatacenter();

    /** @return Tenant that owns the Package */
    String getOwnerTenant();

    /** @return UUID of this Bulk Operation */
    UUID getBulkOperationUuid();

    /** @return The set of resolutions */
    Set<ConflictResolution> getConflictResolutions();

    /** Returns a builder to mutate this BulkOperation */
    Builder mutate();

    /** Returns a new Builder */
    static Builder builder(@Nonnull final String ownerDatacenter, @Nonnull final String ownerTenant, @Nonnull final UUID bulkOperationUuid) {
        return new Builder(ownerDatacenter, ownerTenant, bulkOperationUuid);
    }

    /** Builder class */
    class Builder {
        private final String ownerDatacenter;
        private final String ownerTenant;
        private final UUID bulkOperationUuid;
        private Set<ConflictResolution> conflictResolutions = new HashSet<>();
        private final long optLock;

        Builder(@Nonnull final String ownerDatacenter, @Nonnull final String ownerTenant, @Nonnull final UUID bulkOperationUuid) {
            this.ownerDatacenter = ownerDatacenter;
            this.ownerTenant = ownerTenant;
            this.bulkOperationUuid = bulkOperationUuid;
            this.optLock = 0;
        }

        Builder(@Nonnull final ConflictResolutionSummaryImpl summary) {
            this.ownerDatacenter = summary.getOwnerDatacenter();
            this.ownerTenant = summary.getOwnerTenant();
            this.bulkOperationUuid = summary.getBulkOperationUuid();
            this.conflictResolutions = new HashSet<>(summary.getConflictResolutions());
            this.optLock = summary.getOptLock();
        }

        public Builder setConflictResolutions(@Nonnull final Set<ConflictResolution> conflictResolutions) {
            this.conflictResolutions = conflictResolutions;
            return this;
        }

        public ConflictResolutionSummaryImpl build() {
            return new ConflictResolutionSummaryImpl(ownerDatacenter, ownerTenant, bulkOperationUuid, conflictResolutions, optLock);
        }
    }

}
