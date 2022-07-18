package org.russellrc.playground.domain;

/**
 * Possible actions to take in order
 * to resolve an {@link EntityOperation} that has conflicts
 */
public enum ConflictResolutionOperation {
	APPLY, KEEP_EXISTING
}