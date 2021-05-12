package types;

/**
 * Defines the result of an interpreted query.
 * <p>
 * Do not modify the protocols.
 */
public enum Status {
	/**
	 * Query was recognized (syntactically valid)
	 * and executed without errors (semantically valid).
	 */
	SUCCESSFUL,

	/**
	 * Query was recognized (syntactically valid)
	 * but executed with errors (semantically invalid).
	 */
	FAILED,

	/**
	 * Query was unable to be recognized (syntactically invalid)
	 * and thus unable to be executed at all.
	 */
	UNRECOGNIZED
}
