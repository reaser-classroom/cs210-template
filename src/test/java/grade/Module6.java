package grade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestMethodOrder;

import types.Entry;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Module6 {
	public static final int MAP_OPERATIONS = 5_000;
	public static final int RANDOM_SEED = 2021_06;
	public static final boolean LOG_TO_FILE = false;

	private static int passed;
	private static int hash;
	private static types.HashMap<String, Integer> subject;
	private static java.util.AbstractMap<String, Integer> exemplar;
	private static java.util.Random RNG;
	private static PrintStream LOG_FILE;

	@BeforeAll
	public static final void setup() {
		passed = 0;

		hash = 0;
		try {
			subject = new maps.HashArray<>();
		}
		catch (Exception e) {
			fail("Unexpected constructor exception", e);
		}
		exemplar = new java.util.HashMap<>();

		RNG = new Random(RANDOM_SEED);

		LOG_FILE = null;
		if (LOG_TO_FILE) {
			try {
				LOG_FILE = new PrintStream("m6_map.log");
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	@TestFactory
    @DisplayName("Audits [Required]")
    @Order(0)
    public final Stream<DynamicTest> audits() throws IllegalAccessException {
		final var exempt = List.of("types", "java.lang");

		final var forbidden = new HashSet<Class<?>>();

		for (Class<?> clazz = maps.HashArray.class; clazz != null; clazz = clazz.getSuperclass()) {
			final var fields = new HashSet<Field>();
			Collections.addAll(fields, clazz.getFields());
			Collections.addAll(fields, clazz.getDeclaredFields());

			for (Field f: fields) {
				f.setAccessible(true);
				if (f.get(subject) != null) {
					var type = f.get(subject).getClass();

					while (type.isArray())
						type = type.getComponentType();

					if (!type.isPrimitive() && !exempt.contains(type.getPackage().getName()))
						forbidden.add(type);
				}
				f.setAccessible(false);
			}
		};

		return Stream.of(
    		dynamicTest("No Forbidden Classes", () -> {
    			if (forbidden.size() > 0) {
    				System.err.println("Unexpected forbidden classes:");
    				forbidden.forEach(System.err::println);

    				subject = null;
    				fail("Unexpected forbidden classes <" + forbidden + ">");
    			}
    		})
    	);
    }

	@TestFactory
	@DisplayName("Battery [Totals 100%]")
	@Order(1)
	public final Stream<DynamicTest> battery() {
		if (subject == null)
			fail("Must pass audits before battery");

		logLine("Map<String, Integer> map = new HashArray<>();");

		return RNG.doubles(MAP_OPERATIONS).mapToObj(p -> {
			if (p < 0.70) 	   return testPut();
			else if (p < 0.90) return testRemove();
			else  			   return testGet();
		});
	}

	private static final DynamicTest testPut() {
		final var k = key();
		final var v = value();
		final var call = String.format("put(\"%s\", %s)", k, v);

		return dynamicTest(title(call, k), () -> {
			log(call);

			if (exemplar.containsKey(k))
				hash -= new Entry<>(k, exemplar.get(k)).hashCode();
			hash += new Entry<>(k, v).hashCode();

			assertEquals(
				exemplar.put(k, v),
				subject.put(k, v),
				String.format("%s must yield correct result", call)
			);

			thenTestSize(call);
			thenTestHashCode(call);

			passed++;
		});
	}

	private static final DynamicTest testRemove() {
		final var k = key();
		final var call = String.format("remove(\"%s\")", k);

		return dynamicTest(title(call, k), () -> {
			log(call);

			if (exemplar.containsKey(k))
				hash -= new Entry<>(k, exemplar.get(k)).hashCode();

			assertEquals(
				exemplar.remove(k),
				subject.remove(k),
				String.format("%s must yield correct result", call)
			);

			thenTestSize(call);
			thenTestHashCode(call);

			passed++;
		});
	}

	private static final DynamicTest testGet() {
		final var k = key();
		final var call = String.format("get(\"%s\")", k);

		return dynamicTest(title(call, k), () -> {
			log(call);

			assertEquals(
				exemplar.get(k),
				subject.get(k),
				String.format("%s must yield correct result", call)
			);

			thenTestSize(call);
			thenTestContains(call);

			passed++;
		});
	}

	private static final void thenTestContains(String after) {
		final var k = key();
		final var call = String.format("contains(\"%s\")", k);

		assertEquals(
			exemplar.containsKey(k),
			subject.contains(k),
			String.format("after %s, %s must yield correct result", after, call)
		);
	}

	private static final void thenTestSize(String after) {
		final var call = "size()";

		assertEquals(
			exemplar.size(),
			subject.size(),
			String.format("after %s, %s must yield correct result", after, call)
		);

		thenTestIsEmpty(call);
	}

	private static final void thenTestIsEmpty(String after) {
		final var call = "isEmpty()";

		assertEquals(
			exemplar.isEmpty(),
			subject.isEmpty(),
			String.format("after %s, %s must yield correct result", after, call)
		);
	}

	private static final void thenTestHashCode(String after) {
		final var call = "hashCode()";

		assertEquals(
			hash,
			subject.hashCode(),
			String.format("after %s, %s must yield correct result", after, call)
		);
	}

	@AfterAll
	public static final void report() {
		System.out.printf(
			"[M6 PASSED %d%% OF BATTERY TESTS]\n",
			(int) Math.ceil(passed / (double) MAP_OPERATIONS * 100)
		);
	}

	private static final String key() {
		return Integer.toString((int) (Math.pow(RNG.nextGaussian(), 2) * Math.pow(16, 2)), 16);
	}

	private static final int value() {
		return (int) (Math.pow(RNG.nextGaussian(), 2) * 1000);
	}

	private static final String title(String call, String key) {
		return String.format(
			"%s [%s, m=%d, \u03B1=%.3f]",
			call,
			exemplar.containsKey(key) ? "hit" : "miss",
			subject.size(),
			subject.loadFactor()
		);
	}

	private static final void logLine(String line) {
		if (LOG_FILE != null)
			LOG_FILE.println(line);
	}

	private static final void log(String call) {
		if (LOG_FILE != null)
			LOG_FILE.printf("map.%s;\n", call);
	}
}