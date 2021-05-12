package types;

/**
 * Defines the protocols for a hash table-based map.
 * <p>
 * Do not modify the protocols.
 */
public interface HashMap<K, V> extends Map<K, V> {
	/**
	 * Returns the load factor (alpha), defined
	 * to be the number of entries in the map
	 * divided by the length of the hash table.
	 *
	 * @return the load factor.
	 */
	double loadFactor();

	/**
	 * Returns a hash code of the given key.
	 * <p>
	 * If the key is a string, the hash code is computed
	 * from its characters using an original algorithm.
	 * <p>
	 * Otherwise, the hash code equals the
	 * {@link Object#hashCode()} of the key.
	 *
	 * @param a key.
	 * @return the hash code of the key.
	 */
	int hashFunction(Object key);

	/**
	 * Returns an alternate hash code of the given key.
	 * <p>
	 * The hash code equals <code>p</code> minus the
	 * {@link #hashFunction(Object)} of the key modulo
	 * <code>p</code>, where <code>p</code> is a prime
	 * number less than the length of the hash table.
	 * <p>
	 * This method is optional. Only override it if
	 * the collision resolution technique uses it.
	 *
	 * @param a key.
	 * @return the hash code of the key.
	 */
	default int altHashFunction(Object key) {
		throw new UnsupportedOperationException();
	}
}