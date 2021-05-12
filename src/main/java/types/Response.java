package types;

/**
 * Defines the properties for a response.
 * <p>
 * Do not modify the protocols.
 */
@SuppressWarnings("preview")
public record Response (
	String query,
	Status status,
	String message,
	Table table
) {}