package apps;

import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import drivers.Echo;
import maps.SearchMap;
import types.Driver;
import types.Map;
import types.Response;
import types.Table;

/**
 * This class implements a
 * database management system.
 * <p>
 * Additional protocols may be added.
 */
public class Database implements Closeable {
	private final Map<String, Table> tables;
	private final List<Driver> drivers;

	/**
	 * Initialize the tables and drivers.
	 * <p>
	 * Do not modify the protocol.
	 */
	public Database() {
		this.tables = new SearchMap<>();

		this.drivers = List.of(
			new Echo()
		);
	}

	/**
	 * Returns the tables of this database.
	 * <p>
	 * Do not modify the protocol.
	 *
	 * @return the tables.
	 */
	public Map<String, Table> tables() {
		return tables;
	}

	/**
	 * Interprets a list of queries and returns a list
	 * of responses to each query in sequence.
	 * <p>
	 * Do not modify the protocol.
	 *
	 * @param queries the list of queries.
	 * @return the list of responses.
	 */
	public List<Response> interpret(List<String> queries) {
		List<Response> responses = new LinkedList<>();

		responses.add(drivers.get(0).execute(queries.get(0), this));

		return responses;
	}

	/**
	 * Execute any required tasks when
	 * the database is closed.
	 * <p>
	 * Do not modify the protocol.
	 */
	@Override
	public void close() throws IOException {

	}
}
