package grade;

import static org.junit.jupiter.api.Assertions.*;
import static types.Status.SUCCESSFUL;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import apps.Database;
import maps.SearchMap;
import types.Response;
import types.Status;
import types.Table;

//TODO refactor all first parameters to use Status enums
public class SQLModule {
	protected static int passed;
	protected static Database DB;
	protected static String module_tag;
	protected static Object[][] query_data;
	protected static Object[][] serial_data;
	protected static Table actual_table;

	protected static Arguments[] data() {
		var arguments = new Arguments[query_data.length];

		for (var a = 0; a < arguments.length; a++) {
			Table table = null;

			if (serial_data[a] != null) {
				var i = 0;

				var table_name = (String) serial_data[a][i++];

				var schema_size = (Integer) serial_data[a][i++];
				var primary_index = (Integer) serial_data[a][i++];

				var column_names = new LinkedList<String>();
				for (var j = 1; j <= schema_size; j++)
					column_names.add((String) serial_data[a][i++]);

				var column_types = new LinkedList<String>();
				for (var j = 1; j <= schema_size; j++)
					column_types.add((String) serial_data[a][i++]);

				table = new Table(
					SearchMap.<String, Object>of(
						"table_name", table_name,
						"column_names", column_names,
						"column_types", column_types,
						"primary_index", primary_index
					),
					new SearchMap<Object, List<Object>>()
				);

				for (var j = i; j < serial_data[a].length; j += schema_size) {
					var key = serial_data[a][j+primary_index];
					var value = new LinkedList<>();

					for (var k = 0; k < schema_size; k++)
						value.add(serial_data[a][j+k]);

					table.state().put(key, value);
				}
			}

			arguments[a] = Arguments.of(
				query_data[a][0],
				query_data[a][1],
				query_data[a][2],
				query_data[a][3],
				table
			);
		}

		return arguments;
	}

	@BeforeAll
	protected static final void initialize() throws IOException {
		try {
			DB = new Database();
		}
		catch (Exception e) {
			fail("Database constructor must not throw exceptions", e);
		}

		passed = 0;
	}

	@DisplayName("Queries")
	@ParameterizedTest(name = "[{index}] {2}")
	@MethodSource("data")
	protected void testQuery(
		Status status,
		String table_name,
		String sql,
		String purpose,
		Table expected_table
	) {
		System.out.println(sql);

		var queries = Arrays.asList(sql.split("\\s*;\\s*"));

		List<Response> responses;
		try {
			responses = DB.interpret(queries);
		}
		catch (Exception e) {
			fail("Interpreter must not throw exceptions", e);
			return;
		}

		assertNotNull(
			responses,
			"Interpreter must respond with non-null list,"
		);

		var count = queries.size();

		assertEquals(
			count,
			responses.stream().filter(it -> it != null).count(),
			"Interpreter must respond with one non-null response per query,"
		);

		var last = responses.get(responses.size()-1);

		assertEquals(
			count == 1 ? status : Stream.concat(Stream.generate(() -> SUCCESSFUL).limit(count-1), Stream.of(status)).collect(Collectors.toList()),
			count == 1 ? last.status() : responses.stream().map(it -> it.status()).collect(Collectors.toList()),
			String.format(
				"expected %s to be %s, purpose of test: <%s>, message in response: <%s>,",
				count == 1 ? "query" : "script",
				status.toString().toLowerCase(),
				purpose != null ? purpose : "none provided",
				last.message() != null ? last.message() : "none included"
			)
		);

		actual_table = null;

		String friendly_name = null;
		if (table_name != null) {
			if (table_name.startsWith("_")) {
				actual_table = last.table();
				friendly_name = String.format("result table <%s>", table_name);
			}
			else {
				actual_table = DB.tables().get(table_name);
				friendly_name = String.format("table <%s> in the database", table_name);
			}
		}

		if (expected_table != null) {
			assertNotNull(
				actual_table,
				String.format(
					"%s is null,",
					friendly_name
				)
			);

			if (expected_table.schema() != null) {
				assertNotNull(
					actual_table.schema(),
					String.format(
						"%s has null schema,",
						friendly_name
					)
				);

				assertEquals(
					expected_table.schema().get("table_name"),
					actual_table.schema().get("table_name"),
					String.format(
						"%s has incorrect table name in schema,",
						friendly_name
					)
				);

				assertEquals(
					expected_table.schema().get("column_names"),
					actual_table.schema().get("column_names"),
					String.format(
						"%s has incorrect column names in schema,",
						friendly_name
					)
				);

				assertEquals(
					expected_table.schema().get("column_types"),
					actual_table.schema().get("column_types"),
					String.format(
						"%s has incorrect column types in schema,",
						friendly_name
					)
				);

				assertEquals(
					expected_table.schema().get("primary_index"),
					actual_table.schema().get("primary_index"),
					String.format(
						"%s has incorrect primary index in schema,",
						friendly_name
					)
				);
			}

			if (expected_table.state() != null) {
				assertNotNull(
					actual_table.state(),
					String.format(
						"%s has null state,",
						friendly_name
					)
				);

				for (var entry: expected_table.state()) {
					if (!actual_table.state().contains(entry.key()))
						fail(
							String.format(
								"%s doesn't contain expected primary key <%s> with type <%s> in state",
								friendly_name,
								entry.key(),
								typeOf(entry.key())
							)
						);

					assertEquals(
						stringsOf(expected_table.state().get(entry.key())),
						stringsOf(actual_table.state().get(entry.key())),
						String.format(
							"%s has unexpected values of row with types <%s> for primary key <%s> in state,",
							friendly_name,
							typesOf(entry.value()),
							entry.key()
						)
					);

					assertEquals(
						typesOf(expected_table.state().get(entry.key())),
						typesOf(actual_table.state().get(entry.key())),
						String.format(
							"%s has unexpected types of row with values <%s> for primary key <%s> in state,",
							friendly_name,
							entry.value(),
							entry.key()
						)
					);
				}

				for (var entry: actual_table.state()) {
					if (!expected_table.state().contains(entry.key()))
						fail(
							String.format(
								"%s contains unexpected primary key <%s> with type <%s> in state",
								friendly_name,
								entry.key(),
								typeOf(entry.key())
							)
						);
				}
			}
		}

		passed++;
	}

	@AfterAll
	protected static void report() throws IOException {
		System.out.printf(
			"[%s PASSED %d%% OF UNIT TESTS]\n",
			module_tag,
			(int) Math.ceil(passed / (double) query_data.length * 100)
		);

		try {
			DB.close();
		}
		catch (Exception e) {
			fail("Database close should not throw exceptions", e);
		}
	}

	private static final String typeOf(Object obj) {
		if (obj == null)
			return "null";

		return obj.getClass().getSimpleName().toLowerCase();
	}

	private static final List<String> typesOf(List<Object> list) {
		if (list == null)
			return null;

		return list.stream().map(v -> typeOf(v)).collect(Collectors.toList());
	}

	private static final List<String> stringsOf(List<Object> list) {
		if (list == null)
			return null;

		return list.stream().map(v -> String.valueOf(v)).collect(Collectors.toList());
	}
}