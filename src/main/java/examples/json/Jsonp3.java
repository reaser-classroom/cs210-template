package examples.json;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.stream.JsonGenerator;

public class Jsonp3 {
	public static void main(String[] args) {
		List<List<String>> a = new LinkedList<>();
		a.add(List.of("beta", "zeta", "eta", "theta"));
		a.add(List.of("epsilon", "upsilon"));
		a.add(List.of("pi", "psi", "chi"));
		write(a);
		System.out.println(a);

		List<List<String>> b = read();
		System.out.println(b);

		System.out.println(a.equals(b));
		System.out.println(a == b);
	}

	// Using JSON-P (JSON Processing API)

	public static void write(List<List<String>> lists) {
		try {
			JsonArrayBuilder root_array_builder = Json.createArrayBuilder();
		    for (List<String> inner_list: lists) {
		    	JsonArrayBuilder inner_array_builder = Json.createArrayBuilder();
		    	for (String element: inner_list) {
		    		inner_array_builder.add(element);
		    	}
		    	JsonArray inner_array = inner_array_builder.build();

		    	root_array_builder.add(inner_array);
		    }
		    JsonArray root_array = root_array_builder.build();

		    String filename = "data/jsonp3.json";
		    JsonWriterFactory factory = Json.createWriterFactory(java.util.Map.of(JsonGenerator.PRETTY_PRINTING, true));
		    JsonWriter writer = factory.createWriter(new FileOutputStream(filename));
		    writer.writeArray(root_array);
		    writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<List<String>> read() {
		List<List<String>> lists = null;
		try {
			String filename = "data/jsonp3.json";
			JsonReader reader = Json.createReader(new FileInputStream(filename));
			JsonArray root_array = reader.readArray();
		    reader.close();

		    lists = new LinkedList<>();
		    for (int i = 0; i < root_array.size(); i++) {
		    	JsonArray inner_array = root_array.getJsonArray(i);
		    	List<String> inner_list = new LinkedList<>();
		    	for (int j = 0; j < inner_array.size(); j++) {
		    		inner_list.add(inner_array.getString(j));
		    	}
		    	lists.add(inner_list);
		    }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return lists;
	}
}
