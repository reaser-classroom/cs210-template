package types;

import java.util.List;

/**
 * Defines the properties for a table.
 * <p>
 * Do not modify the protocols.
 */
@SuppressWarnings("preview")
public record Table (
	Map<String, Object> schema,
	Map<Object, List<Object>> state
) {}
