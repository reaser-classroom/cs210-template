package examples.binary;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

import maps.SearchMap;
import types.Entry;
import types.Map;

public class Bytes2 {
	public static void main(String[] args) {
		Map<String, List<String>> a = new SearchMap<>();
		a.put("beta", List.of("beta", "zeta", "eta", "theta"));
		a.put("epsilon", List.of("epsilon", "upsilon"));
		a.put("pi", List.of("pi", "psi", "chi"));
		write(a);
		System.out.println(a);

		Map<String, List<String>> b = read();
		System.out.println(b);

		System.out.println(a.equals(b));
		System.out.println(a == b);
	}

	public static void write(Map<String, List<String>> map) {
		try (
			final FileChannel channel = FileChannel.open(
				Paths.get("data", "bytes2.bin"),
				StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING, // CAUTION
				StandardOpenOption.SPARSE, // CAUTION
				StandardOpenOption.READ,
				StandardOpenOption.WRITE
			)
		) {
			final int set_size = map.size(),
				MAX_LIST = 4,
				MAX_STRING = 7,
				KEY_INDEX = 0;

			MappedByteBuffer buf = channel.map(
				FileChannel.MapMode.READ_WRITE,
				0,
				4 + set_size*(MAX_LIST*(1 + MAX_STRING))
			);

			buf.putInt(set_size);

		    for (Entry<String, List<String>> entry: map) {
		    	final String expected_key = entry.key();
		    	final String actual_key = entry.value().get(KEY_INDEX);
		    	if (expected_key == null ? actual_key != null : !expected_key.equals(actual_key))
		    		throw new IllegalStateException();

		    	final int strings = entry.value().size();
		    	for (int j = 0; j < strings; j++) {
			    	final byte[] chars = entry.value().get(j).getBytes(StandardCharsets.UTF_8);
			    	buf.put((byte) chars.length);
			    	buf.put(chars);
			    	buf.position(buf.position() + MAX_STRING - chars.length);
//			    	buf.put(new byte[MAX_STRING - chars.length]);
		    	}
		    	if (strings < MAX_LIST) {
		    		buf.put((byte) -1);
		    		buf.position(buf.position() - 1 + (MAX_LIST-strings) * (1 + MAX_STRING));
		    	}
		    }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, List<String>> read() {
		Map<String, List<String>> map = null;
		try (
			final FileChannel channel = FileChannel.open(
				Paths.get("data", "bytes2.bin"),
				StandardOpenOption.READ
			)
		) {
			MappedByteBuffer buf = channel.map(
				FileChannel.MapMode.READ_ONLY,
				0,
				4
			);

			final int set_size = buf.getInt(),
				MAX_LIST = 4,
				MAX_STRING = 7,
				KEY_INDEX = 0;

			buf = channel.map(
				FileChannel.MapMode.READ_ONLY,
				4,
				set_size*(MAX_LIST*(1 + MAX_STRING))
			);

			map = new SearchMap<>();
		    for (int i = 0; i < set_size; i++) {
		    	final List<String> list = new LinkedList<>();
		    	for (int j = 0; j < MAX_LIST; j++) {
		    		final int len = buf.get();
		    		if (len == -1) break;

					final byte[] chars = new byte[len];
					buf.get(chars);
					buf.position(buf.position() + MAX_STRING - chars.length);

					list.add(new String(chars, StandardCharsets.UTF_8));
	    		}
		    	if (list.size() < MAX_LIST)
		    		buf.position(buf.position() - 1 + (MAX_LIST-list.size()) * (1 + MAX_STRING));

		    	map.put(list.get(KEY_INDEX), list);
		    }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
}
