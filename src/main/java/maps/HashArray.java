package maps;

import java.util.Iterator;

import types.Entry;
import types.HashMap;

/**
 * Implements a hash table-based map
 * using an array data structure.
 */
public class HashArray<K, V> implements HashMap<K, V> {
	/*
	 * TODO: For Module 6, implement the stubs.
	 *
	 * Until then, this class is unused.
	 */
	public HashArray() {
		throw new UnsupportedOperationException("HashArray is unimplemented");
	}

	@Override
	public void clear() {

	}

	@Override
	public V put(K key, V value) {
		return null;
	}

	@Override
	public V get(K key) {
		return null;
	}

	@Override
	public V remove(K key) {
		return null;
	}

	@Override
	public boolean contains(K key) {
		return false;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public boolean equals(Object o) {
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public double loadFactor() {
		return 0;
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return null;
	}

	@Override
	public int hashFunction(Object key) {
		return 0;
	}

	@Override
	public int altHashFunction(Object key) {
		return 0;
	}
}
