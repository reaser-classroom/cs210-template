package types;

/** 
 * Defines the properties for an entry.
 * <p>
 * Do not modify the protocols.
 */
@SuppressWarnings("preview")
public record Entry<K, V> (
	K key,
	V value
) {}