package drivers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import apps.Database;
import types.Driver;
import types.Response;
import types.Status;

/*
 * Example:
 *   ECHO "Hello, world!"
 *
 * Response:
 * 	 query: ECHO "Hello, world!"
 *   successful
 *   message: "Hello, world!"
 *   no result table
 */
public class Echo implements Driver {
	static final Pattern pattern = Pattern.compile(
		"ECHO\\s*\"([^\"]*)\"",
		Pattern.CASE_INSENSITIVE
	);

	@Override
	public Response execute(String query, Database db) {
		Matcher matcher = pattern.matcher(query.strip());
		if (!matcher.matches())
			return new Response(query, Status.UNRECOGNIZED, null, null);

		String text = matcher.group(1);

		return new Response(query, Status.SUCCESSFUL, text, null);
	}
}
