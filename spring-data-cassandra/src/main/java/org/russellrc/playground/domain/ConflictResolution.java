package org.russellrc.playground.domain;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * The UUID of an {@link EntityOperation} in conflict and the
 * {@link ConflictResolutionOperation} that will be used to resolve it
 */
@UserDefinedType("conflict_resolution")
public class ConflictResolution implements Comparable<ConflictResolution> {

    public static final Comparator<ConflictResolution> COMPARATOR = Comparator.comparing(ConflictResolution::getEntityOperationUuid);

    @Column("entity_operation_uuid")
    @CassandraType(type = CassandraType.Name.UUID)
    private final UUID entityOperationUuid;

    @Column("conflict_resolution_operation")
    @Nullable
    private final ConflictResolutionOperation conflictResolutionOperation;


    /** Constructor */
    public ConflictResolution(@Nonnull final UUID entityOperationUuid, @Nonnull final ConflictResolutionOperation conflictResolutionOperation) {
        this.entityOperationUuid = entityOperationUuid;
        this.conflictResolutionOperation = conflictResolutionOperation;
    }

    public UUID getEntityOperationUuid() {
        return entityOperationUuid;
    }

    public Optional<ConflictResolutionOperation> getConflictResolutionOperation() {
        return Optional.ofNullable(conflictResolutionOperation);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConflictResolution)) {
            return false;
        }
        final ConflictResolution that = (ConflictResolution) o;
        return Objects.equals(entityOperationUuid, that.entityOperationUuid) &&
            Objects.equals(conflictResolutionOperation, that.conflictResolutionOperation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityOperationUuid, conflictResolutionOperation);
    }

    @Override
    public int compareTo(final ConflictResolution o) {
        return COMPARATOR.compare(this, o);
    }

    @Override
    public String toString() {
        return "ConflictResolution{" +
            "entityOperationUuid=" + entityOperationUuid +
            ", conflictResolutionOperation=" + conflictResolutionOperation +
            '}';
    }
}
