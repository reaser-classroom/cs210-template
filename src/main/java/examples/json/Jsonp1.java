package examples.json;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;

import maps.SearchMap;
import types.Entry;
import types.Map;

public class Jsonp1 {
	public static void main(String[] args) {
		Map<String, Integer> a = new SearchMap<>();
		a.put("alpha", 1);
		a.put("beta",  2);
		a.put("gamma", 3);
		a.put("delta", 4);
		a.put("tau",   19);
		a.put("pi",    16);
		write(a);
		System.out.println(a);

		Map<String, Integer> b = read();
		System.out.println(b);

		System.out.println(a.equals(b));
		System.out.println(a == b);
	}

	// Using JSON-P (JSON Processing API)

	public static void write(Map<String, Integer> map) {
		try {
		    JsonObjectBuilder root_object_builder = Json.createObjectBuilder();
		    for (Entry<String, Integer> entry: map) {
		    	root_object_builder.add(entry.key(), entry.value());
		    }
		    JsonObject root_object = root_object_builder.build();

		    String filename = "data/jsonp1.json";
		    JsonWriter writer = Json.createWriter(new FileOutputStream(filename));
		    writer.writeObject(root_object);
		    writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Integer> read() {
		Map<String, Integer> map = null;
		try {
			String filename = "data/jsonp1.json";
			JsonReader reader = Json.createReader(new FileInputStream(filename));
		    JsonObject root_object = reader.readObject();
		    reader.close();

		    map = new SearchMap<>();
		    for (String key: root_object.keySet()) {
		    	map.put(key, root_object.getInt(key));
		    }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
}
