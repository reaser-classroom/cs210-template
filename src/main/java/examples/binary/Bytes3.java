package examples.binary;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

public class Bytes3 {
	private static FileChannel channel;

	public static void main(String[] args) {
		try {
			channel = FileChannel.open(
				Paths.get("data", "bytes3.bin"),
				StandardOpenOption.CREATE,
				StandardOpenOption.SPARSE, // CAUTION
				StandardOpenOption.READ,
				StandardOpenOption.WRITE
			);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		writeSize(3);
		write(0, List.of("beta", "zeta", "eta", "theta"));
		write(1, List.of("epsilon", "upsilon"));
		write(2, List.of("pi", "psi", "chi"));

		System.out.println(readSize());
		System.out.println(read(0));
		System.out.println(read(1));
		System.out.println(read(2));
	}

	public static void writeSize(int size) {
		try {
			final MappedByteBuffer buf = channel.map(
				FileChannel.MapMode.READ_WRITE,
				0,
				4
			);

	    	buf.putInt(size);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void write(int index, List<String> list) {
		if (index >= readSize())
			throw new IndexOutOfBoundsException();

		try {
			final int MAX_LIST = 4,
				MAX_STRING = 7;

			final MappedByteBuffer buf = channel.map(
				FileChannel.MapMode.READ_WRITE,
				4 + index * (1 + MAX_LIST*(1 + MAX_STRING)),
				1 + MAX_LIST*(1 + MAX_STRING)
			);

	    	final int strings = list.size();

			byte mask = 0;
			for (int j = 0; j < MAX_LIST; j++)
				mask = (byte) ((mask << 1) + (j < strings ? 1 : 0));
			buf.put(mask);

	    	for (int j = 0; j < strings; j++) {
		    	final byte[] chars = list.get(j).getBytes(StandardCharsets.UTF_8);
		    	buf.put((byte) chars.length);
		    	buf.put(chars);
		    	buf.position(buf.position() + MAX_STRING - chars.length);
	    	}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int readSize() {
		int size = -1;
		try {
			final MappedByteBuffer buf = channel.map(
				FileChannel.MapMode.READ_ONLY,
				0,
				4
			);

			size = buf.getInt();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return size;
	}

	public static List<String> read(int index) {
		if (index >= readSize())
			throw new IndexOutOfBoundsException();

		List<String> list = null;
		try {
			final int MAX_LIST = 4,
				MAX_STRING = 7;

			final MappedByteBuffer buf = channel.map(
				FileChannel.MapMode.READ_ONLY,
				4 + index * (1 + MAX_LIST*(1 + MAX_STRING)),
				1 + MAX_LIST*(1 + MAX_STRING)
			);

			final byte mask = buf.get();

	    	list = new LinkedList<>();
	    	for (int j = 0; j < MAX_LIST; j++) {
	    		if ((mask & 1 << (MAX_LIST-1-j)) == 0)
	    			break;

	    		final int len = buf.get();
				final byte[] chars = new byte[len];
				buf.get(chars);
				buf.position(buf.position() + MAX_STRING - chars.length);

				list.add(new String(chars, StandardCharsets.UTF_8));
    		}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
