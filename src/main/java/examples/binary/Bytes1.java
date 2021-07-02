package examples.binary;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import maps.SearchMap;
import types.Entry;
import types.Map;

public class Bytes1 {
	public static void main(String[] args) {
		Map<String, Integer> a = new SearchMap<>();
		a.put("alpha", 1);
		a.put("beta",  2);
		a.put("gamma", 3);
		a.put("delta", 4);
		a.put("tau",   19);
		a.put("pi",    16);
		write(a);
		System.out.println(a);

		Map<String, Integer> b = read();
		System.out.println(b);

		System.out.println(a.equals(b));
		System.out.println(a == b);
	}

	public static void write(Map<String, Integer> map) {
		try (
			final FileChannel channel = FileChannel.open(
				Paths.get("data", "bytes1.bin"),
				StandardOpenOption.CREATE,
				StandardOpenOption.READ,
				StandardOpenOption.WRITE
			)
		) {
			final int map_size = map.size(),
				MAX_STRING = 8;

			MappedByteBuffer buf = channel.map(
				FileChannel.MapMode.READ_WRITE,
				0,
				4 + 4 + map_size*(4 + MAX_STRING + 4)
			);

			buf.putInt(map_size);
			buf.putInt(MAX_STRING);

		    for (Entry<String, Integer> entry: map) {
		    	final byte[] chars = entry.key().getBytes(StandardCharsets.UTF_8);
		    	buf.putInt(chars.length);
		    	buf.put(chars);
		    	buf.put(new byte[MAX_STRING - chars.length]);
		    	buf.putInt(entry.value());
		    }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Integer> read() {
		Map<String, Integer> map = null;
		try (
			final FileChannel channel = FileChannel.open(
				Paths.get("data", "bytes1.bin"),
				StandardOpenOption.READ
			)
		) {
			MappedByteBuffer buf = channel.map(
				FileChannel.MapMode.READ_ONLY,
				0,
				4 + 4
			);

			final int map_size = buf.getInt(),
				MAX_STRING = buf.getInt();

			buf = channel.map(
				FileChannel.MapMode.READ_ONLY,
				4 + 4,
				map_size*(4 + MAX_STRING + 4)
			);

			map = new SearchMap<>();
			for (int i = 0; i < map_size; i++) {
				final byte[] chars = new byte[buf.getInt()];
				buf.get(chars);
				buf.position(buf.position() + MAX_STRING - chars.length);
				map.put(
		    		new String(chars, StandardCharsets.UTF_8),
		    		buf.getInt()
		    	);
		    }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
}
