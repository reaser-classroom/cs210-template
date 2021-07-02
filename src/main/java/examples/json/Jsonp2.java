package examples.json;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.stream.JsonGenerator;

import maps.SearchMap;
import types.Entry;
import types.Map;

public class Jsonp2 {
	public static void main(String[] args) {
		Map<String, List<String>> a = new SearchMap<>();
		a.put("beta", List.of("beta", "zeta", "eta", "theta"));
		a.put("epsilon", List.of("epsilon", "upsilon"));
		a.put("pi", List.of("pi", "psi", "chi"));
		write(a);
		System.out.println(a);

		Map<String, List<String>> b = read();
		System.out.println(b);

		System.out.println(a.equals(b));
		System.out.println(a == b);
	}

	// Using JSON-P (JSON Processing API)

	public static void write(Map<String, List<String>> map) {
		try {
			JsonObjectBuilder root_object_builder = Json.createObjectBuilder();
		    for (Entry<String, List<String>> entry: map) {
		    	JsonArrayBuilder array_builder = Json.createArrayBuilder();
		    	for (String element: entry.value()) {
		    		array_builder.add(element);
		    	}
		    	JsonArray array = array_builder.build();

		    	root_object_builder.add(entry.key(), array);
		    }
		    JsonObject root_object = root_object_builder.build();

		    String filename = "data/jsonp2.json";
		    JsonWriterFactory factory = Json.createWriterFactory(java.util.Map.of(JsonGenerator.PRETTY_PRINTING, true));
		    JsonWriter writer = factory.createWriter(new FileOutputStream(filename));
		    writer.writeObject(root_object);
		    writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, List<String>> read() {
		Map<String, List<String>> map = null;
		try {
			String filename = "data/jsonp2.json";
			JsonReader reader = Json.createReader(new FileInputStream(filename));
		    JsonObject root_object = reader.readObject();
		    reader.close();

		    map = new SearchMap<>();
		    for (String key: root_object.keySet()) {
		    	JsonArray array = root_object.getJsonArray(key);
		    	List<String> list = new LinkedList<>();
		    	for (int i = 0; i < array.size(); i++) {
		    		list.add(array.getString(i));
		    	}
		    	map.put(key, list);
		    }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
}
