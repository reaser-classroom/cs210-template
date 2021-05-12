package types;

import apps.Database;

/**
 * Defines the protocols for query drivers.
 * <p>
 * Additional protocols may be added.
 */
public interface Driver {
	/**
	 * Executes the given query against the
	 * given database and returns a response
	 * indicating the result of the query.
	 * <p>
	 * Do not modify the protocol.
	 *
	 * @param query the query to execute.
	 * @param db the database to execute against.
	 * @return the response of the query.
	 **/
	Response execute(String query, Database db);
}
