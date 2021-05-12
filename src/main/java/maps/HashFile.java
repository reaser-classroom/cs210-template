package maps;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

import types.Entry;
import types.HashMap;

/**
 * Implements a hash table-based map
 * using a random access file structure.
 */
public class HashFile implements HashMap<Object, List<Object>> {
	/*
	 * TODO: For Module 7, implement the stubs.
	 *
	 * Until then, this class is unused.
	 */
	public HashFile(Path path, Entry<Integer, List<String>> descriptor) {
		throw new UnsupportedOperationException("HashFile is unimplemented");
	}

	@Override
	public void clear() {

	}

	@Override
	public List<Object> put(Object key, List<Object> value) {
		return null;
	}

	@Override
	public List<Object> get(Object key) {
		return null;
	}

	@Override
	public List<Object> remove(Object key) {
		return null;
	}

	@Override
	public boolean contains(Object key) {
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
	public Iterator<Entry<Object, List<Object>>> iterator() {
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
