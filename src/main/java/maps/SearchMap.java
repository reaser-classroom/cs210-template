package maps;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import types.Entry;
import types.Map;

/**
 * Implements a search-based map
 * using a list data structure.
 */
public class SearchMap<K, V> implements Map<K, V> {
	private List<Entry<K, V>> list;

	public SearchMap() {
		list = new LinkedList<>();
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public V put(K key, V value) {
		for (int i = 0; i < list.size(); i++) {
			Entry<K, V> entry = list.get(i);
			if (key == null ? entry.key() == null : key.equals(entry.key())) {
				list.set(i, new Entry<>(key, value));
				return entry.value();
			}
		}
		list.add(new Entry<>(key, value));
		return null;
	}

	@Override
	public V get(K key) {
		for (int i = 0; i < list.size(); i++) {
			Entry<K, V> entry = list.get(i);
			if (key == null ? entry.key() == null : key.equals(entry.key()))
				return entry.value();
		}
		return null;
	}

	@Override
	public V remove(K key) {
		for (int i = 0; i < list.size(); i++) {
			Entry<K, V> entry = list.get(i);
			if (key == null ? entry.key() == null : key.equals(entry.key())) {
				list.remove(i);
				return entry.value();
			}
		}
		return null;
	}

	@Override
	public boolean contains(K key) {
		for (int i = 0; i < list.size(); i++) {
			Entry<K, V> entry = list.get(i);
			if (key == null ? entry.key() == null : key.equals(entry.key()))
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return list.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Map<?, ?>)
			return this.hashCode() == o.hashCode();
		else return false;
	}

	@Override
	public int hashCode() {
		int sum = 0;
		for (int i = 0; i < list.size(); i++)
			sum += list.get(i).hashCode();
		return sum;
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return new Iterator<>() {
			int index = 0;

			@Override
			public boolean hasNext() {
				return index < list.size();
			}

			@Override
			public Entry<K, V> next() {
				return list.get(index++);
			}
		};
	}

	/**
	 * Factory method to generate a new search map
	 * from a sequence of interleaved keys and values.
	 * <p>
	 * The first two elements are the key and value
	 * for the first entry, the next two are the key
	 * and value for the second entry, and so on.
	 *
	 * @param <K> the key type of the new search map.
	 * @param <V> the value type of the new search map.
	 * @param elements the interleaved keys and values.
	 * @return the new search map.
	 *
	 * @throws IllegalArgumentException if
	 * the number of elements isn't even.
	 * @throws ClassCastException if
	 * the elements aren't the correct types.
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> of(Object... elements) {
		if (elements.length % 2 != 0)
			throw new IllegalArgumentException();

		Map<K, V> map = new SearchMap<>();
		for (int i = 0; i < elements.length; i += 2)
			map.put((K) elements[i], (V) elements[i+1]);

		return map;
	}
}