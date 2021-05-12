package grade;

import static types.Status.*;

import org.junit.jupiter.api.BeforeAll;

@Deprecated
public class Lab1 extends SQLModule {
	@BeforeAll
	public static void setup() {
		module_tag = "L1";

		query_data = new Object[][]{
			// DEFAULT COLUMNS
			{ SUCCESSFUL, "_squares", "SQUARES BELOW 20", null },
			{ SUCCESSFUL, "_squares", "SQUARES BELOW 16", "the integer is a strict upper bound" },
			{ SUCCESSFUL, "_squares", "SQUARES BELOW 0", "there are no non-positive squares" },
			{ SUCCESSFUL, "_squares", "SQUARES BELOW 1000", "the integer data type is unbounded" },

			// BASE NAME WITH DEFAULT POWER NAME
			{ SUCCESSFUL, "_squares", "SQUARES BELOW 500 AS a", null },
			{ SUCCESSFUL, "_squares", "SQUARES BELOW 100 AS a", null },
			{ SUCCESSFUL, "_squares", "SQUARES BELOW 100 AS x", "custom name can be the default name" },

			// BASE AND POWER NAMES
			{ SUCCESSFUL, "_squares", "SQUARES BELOW 1000 AS a, b", null },
			{ SUCCESSFUL, "_squares", "SQUARES BELOW 50 AS a, b", null },
			{ FAILED, "_squares", "SQUARES BELOW 50 AS a, a", "custom column names must be different" },

			// SYNTAX
			{ SUCCESSFUL, "_squares", "squares below 20 as A, B", "keywords are not case sensitive" },
			{ SUCCESSFUL, "_squares", "SQUARES  BELOW  20 AS a ,b ", "additional whitespace is permitted" },
			{ UNRECOGNIZED, "_squares", "SQUARESBELOW20ASa,b", "delineating whitespace is required" },
			{ UNRECOGNIZED, "_squares", "SQUARES 20", "the BELOW keyword is required" },
			{ UNRECOGNIZED, "_squares", "BELOW 20", "the SQUARES keyword is required" },
			{ UNRECOGNIZED, "_squares", "SQUARES BELOW AS a, b", "the integer is required" },
			{ UNRECOGNIZED, "_squares", "SQUARES BELOW 20 AS a b", "the comma is required before a power name" },
			{ UNRECOGNIZED, "_squares", "SQUARES BELOW 20 AS a,", "the comma is forbidden without a power name" },
			{ UNRECOGNIZED, "_squares", "SQUARES BELOW 20 AS , b", "the base name is required" },

			// ROBUSTNESS
			{ SUCCESSFUL, null, "ECHO \"Hello, world!\"", "existing drivers should still function" }
		};

		serial_data = new Object[][]{
			{ "_squares", 2, 0, "x", "x_squared", "integer", "integer", 0, 0, 1, 1, 2, 4, 3, 9, 4, 16 },
			{ "_squares", 2, 0, "x", "x_squared", "integer", "integer", 0, 0, 1, 1, 2, 4, 3, 9 },
			{ "_squares", 2, 0, "x", "x_squared", "integer", "integer" },
			{ "_squares", 2, 0, "x", "x_squared", "integer", "integer", 0, 0, 1, 1, 2, 4, 3, 9, 4, 16, 5, 25, 6, 36, 7, 49, 8, 64, 9, 81, 10, 100, 11, 121, 12, 144, 13, 169, 14, 196, 15, 225, 16, 256, 17, 289, 18, 324, 19, 361, 20, 400, 21, 441, 22, 484, 23, 529, 24, 576, 25, 625, 26, 676, 27, 729, 28, 784, 29, 841, 30, 900, 31, 961 },
			{ "_squares", 2, 0, "a", "a_squared", "integer", "integer", 0, 0, 1, 1, 2, 4, 3, 9, 4, 16, 5, 25, 6, 36, 7, 49, 8, 64, 9, 81, 10, 100, 11, 121, 12, 144, 13, 169, 14, 196, 15, 225, 16, 256, 17, 289, 18, 324, 19, 361, 20, 400, 21, 441, 22, 484 },
			{ "_squares", 2, 0, "a", "a_squared", "integer", "integer", 0, 0, 1, 1, 2, 4, 3, 9, 4, 16, 5, 25, 6, 36, 7, 49, 8, 64, 9, 81 },
			{ "_squares", 2, 0, "x", "x_squared", "integer", "integer", 0, 0, 1, 1, 2, 4, 3, 9, 4, 16, 5, 25, 6, 36, 7, 49, 8, 64, 9, 81 },
			{ "_squares", 2, 0, "a", "b", "integer", "integer", 0, 0, 1, 1, 2, 4, 3, 9, 4, 16, 5, 25, 6, 36, 7, 49, 8, 64, 9, 81, 10, 100, 11, 121, 12, 144, 13, 169, 14, 196, 15, 225, 16, 256, 17, 289, 18, 324, 19, 361, 20, 400, 21, 441, 22, 484, 23, 529, 24, 576, 25, 625, 26, 676, 27, 729, 28, 784, 29, 841, 30, 900, 31, 961 },
			{ "_squares", 2, 0, "a", "b", "integer", "integer", 0, 0, 1, 1, 2, 4, 3, 9, 4, 16, 5, 25, 6, 36, 7, 49 },
			null,
			{ "_squares", 2, 0, "A", "B", "integer", "integer", 0, 0, 1, 1, 2, 4, 3, 9, 4, 16 },
			{ "_squares", 2, 0, "a", "b", "integer", "integer", 0, 0, 1, 1, 2, 4, 3, 9, 4, 16 },
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null
		};
	}
}