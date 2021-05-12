package types;

import java.util.Iterator;

/**
 * Defines the protocols for a map.
 * <p>
 * Do not modify the protocols.
 */
public interface Map<K, V> extends Iterable<Entry<K, V>> {
	/**
	 * Removes all entries from the map.
	 */
	void clear();

	/**
	 * If an entry with the given key doesn't exist,
	 * adds a new entry with the given key and value.
	 * <p>
	 * Otherwise, if an entry with the given key exists,
	 * updates that entry with the given value.
	 * <p>
	 * Returns the old value for the given key,
	 * or <code>null</code> if the key is new.
	 *
	 * @param key a key.
	 * @param value a value.
	 * @return the value for the key
	 * 		or <code>null</code> by default.
	 */
	V put(K key, V value);

	/**
	 * Returns the value for the given key,
	 * or <code>null</code> if the key doesn't exist.
	 *
	 * @param key a key.
	 * @return the value for the key
	 * 		or <code>null</code> by default.
	 */
	V get(K key);

	/**
	 * If an entry with the given key exists,
	 * removes the entry.
	 * <p>
	 * Returns the old value for the given key,
	 * or <code>null</code> if the key doesn't exist.
	 *
	 * @param key a key.
	 * @return the old value for the key
	 * 		or <code>null</code> by default.
	 */
	V remove(K key);

	/**
	 * Returns whether the given key exists.
	 *
	 * @param key a key.
	 * @return <code>true</code> if the key exists
	 * 		or <code>false</code> otherwise.
	 */
	boolean contains(K key);

	/**
	 * Returns the number of entries.
	 *
	 * @return the number of entries.
	 */
	int size();

	/**
	 * Returns whether there are no entries.
	 *
	 * @return <code>true</code> if there are no entries
	 * 		or <code>false</code> otherwise.
	 */
	boolean isEmpty();

	/**
	 * Returns a string representation of this map as
	 * a pair of curly braces enclosing a comma-separated
	 * arrangement of all {@link Entry#toString()} in this map.
	 *
	 * @return a string representation of this map.
	 */
	@Override
	String toString();

	/**
	 * Returns whether the given object is also a map
	 * and has the same hash code as this map, thus
	 * indicating with astronomically high probability
	 * whether this map equals the given object.
	 *
	 * @param an object.
	 * @return whether this map equals the given object.
	 */
	@Override
	boolean equals(Object o);

	/**
	 * Returns the sum of the hash codes of all entries
	 * in this map as the hash code of this map.
	 *
	 * @return this map's hash code.
	 */
	@Override
	int hashCode();

	/**
	 * Returns an iterator over each entry of this map.
	 *
	 * @return an iterator.
	 */
	@Override
	Iterator<Entry<K, V>> iterator();
}