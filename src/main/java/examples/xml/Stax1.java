package examples.xml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import maps.SearchMap;
import types.Entry;
import types.Map;

public class Stax1 {
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

	// Using StAX (Streaming API for XML)

	public static void write(Map<String, Integer> map) {
		try {
			String filename = "data/stax1.xml";
			XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileWriter(filename));

			writer.writeStartDocument();
			writer.writeStartElement("map");
			for (Entry<String, Integer> entry: map) {
				writer.writeStartElement("entry");
				writer.writeAttribute("key", entry.key().toString());
				writer.writeCharacters(entry.value().toString());
				writer.writeEndElement();
			}
			writer.writeEndElement();
			writer.writeEndDocument();

			writer.close();
		}
		catch (XMLStreamException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Integer> read() {
		try {
			String filename = "data/stax1.xml";
			XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileReader(filename));

			Map<String, Integer> map = null;
			String key = null;
			Integer value = null;
			while (reader.hasNext()) {
				reader.next();

				if (reader.getEventType() == XMLStreamReader.START_ELEMENT) {
					if (reader.getLocalName().equalsIgnoreCase("map")) {
						map = new SearchMap<>();
					}
					else if (reader.getLocalName().equalsIgnoreCase("entry")) {
						key = reader.getAttributeValue(null, "key");

						// Caution: getElementText advances to the end element event.
						value = Integer.parseInt(reader.getElementText());
					}
				}

				// Caution: must use if, else-if would skip an end element event.
				if (reader.getEventType() == XMLStreamReader.END_ELEMENT) {
					if (reader.getLocalName().equalsIgnoreCase("entry")) {
						map.put(key, value);
						key = null;
						value = null;
					}
					else if (reader.getLocalName().equalsIgnoreCase("map")) {
						// any necessary finalization
					}
				}
			}

			reader.close();
			return map;
		}
		catch (XMLStreamException e) {
			e.printStackTrace();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
