package apps;

import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import types.Response;

/**
 * Implements a user console for
 * interacting with a database.
 * <p>
 * Additional protocols may be added.
 */
public class Console {
	/**
	 * The entry point for execution
	 * with user input/output.
	 *
	 * @param args ignored.
	 */
	public static void main(String[] args) {
		try (
			final Database db = new Database();
			final Scanner in = new Scanner(System.in);
			final PrintStream out = System.out;
		)
		{
			out.print(">> ");

			String text = in.nextLine();

			List<String> queries = new LinkedList<>();
			queries.add(text);

			List<Response> responses = db.interpret(queries);

			out.println("Query:   " + responses.get(0).query());
			out.println("Status:  " + responses.get(0).status());
			out.println("Message: " + responses.get(0).message());

			// TODO: For Module 4, pretty-print the table.
			out.println("Table:   " + responses.get(0).table());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
