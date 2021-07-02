package examples.xml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import maps.SearchMap;
import types.Entry;
import types.Map;

public class Stax2 {
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

	// Using StAX (Streaming API for XML)

	public static void write(Map<String, List<String>> map) {
		try {
			StringWriter output = new StringWriter();
			XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(output);

			writer.writeStartDocument();
			writer.writeStartElement("map");
		    for (Entry<String, List<String>> entry: map) {
				writer.writeStartElement("entry");
				writer.writeAttribute("key", entry.key());
		    	for (String element: entry.value()) {
					writer.writeEmptyElement("value");
					writer.writeAttribute("string-data", element.toString());
		    	}
				writer.writeEndElement();
		    }
			writer.writeEndElement();
			writer.writeEndDocument();

			writer.close();

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
		    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		    String filename = "data/stax2.xml";
		    Source from = new StreamSource(new StringReader(output.toString()));
		    Result to = new StreamResult(new FileWriter(filename));
		    transformer.transform(from, to);
		}
		catch (XMLStreamException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Map<String, List<String>> read() {
		try {
			String filename = "data/stax2.xml";
			XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileReader(filename));

			Map<String, List<String>> map = null;
			String key = null;
			List<String> list = null;
			String list_value = null;
			while (reader.hasNext()) {
				reader.next();

				if (reader.getEventType() == XMLStreamReader.START_ELEMENT) {
					if (reader.getLocalName().equalsIgnoreCase("map")) {
						map = new SearchMap<>();
					}
					else if (reader.getLocalName().equalsIgnoreCase("entry")) {
						key = reader.getAttributeValue(null, "key");
						list = new LinkedList<>();
					}
					else if (reader.getLocalName().equalsIgnoreCase("value")) {
						list_value = reader.getAttributeValue(null, "string-data");
					}
				}

				if (reader.getEventType() == XMLStreamReader.END_ELEMENT) {
					if (reader.getLocalName().equalsIgnoreCase("value")) {
						list.add(list_value);
						list_value = null;
					}
					else if (reader.getLocalName().equalsIgnoreCase("entry")) {
						map.put(key, list);
						key = null;
						list = null;
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
