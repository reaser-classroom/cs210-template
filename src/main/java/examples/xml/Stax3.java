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

public class Stax3 {
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

	// Using StAX (Streaming API for XML)

	public static void write(List<List<String>> lists) {
		try {
			StringWriter output = new StringWriter();
			XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(output);

			writer.writeStartDocument();
			writer.writeStartElement("lists");
			writer.writeAttribute("size", Integer.toString(lists.size()));
		    for (List<String> inner_list: lists) {
				writer.writeStartElement("list");
				writer.writeAttribute("size", Integer.toString(inner_list.size()));
		    	for (String element: inner_list) {
					writer.writeStartElement("value");
					writer.writeAttribute("type", "string");
					writer.writeAttribute("length", Integer.toString(element.toString().length()));
					writer.writeCharacters(element.toString());
					writer.writeEndElement();
		    	}
				writer.writeEndElement();
		    }
			writer.writeEndElement();
			writer.writeEndDocument();

			writer.close();

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
		    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		    String filename = "data/stax3.xml";
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

	public static List<List<String>> read() {
		try {
			String filename = "data/stax3.xml";
			XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileReader(filename));

			List<List<String>> lists = null;
			List<String> list = null;
			String value = null;
			while (reader.hasNext()) {
				reader.next();

				if (reader.getEventType() == XMLStreamReader.START_ELEMENT) {
					if (reader.getLocalName().equalsIgnoreCase("lists")) {
						lists = new LinkedList<>();
					}
					else if (reader.getLocalName().equalsIgnoreCase("list")) {
						list = new LinkedList<>();
					}
					else if (reader.getLocalName().equalsIgnoreCase("value")) {
						if (reader.getAttributeValue(null, "type").equals("string"))
							value = new String(reader.getElementText());
					}
				}

				if (reader.getEventType() == XMLStreamReader.END_ELEMENT) {
					if (reader.getLocalName().equalsIgnoreCase("value")) {
						list.add(value);
						value = null;
					}
					else if (reader.getLocalName().equalsIgnoreCase("list")) {
						lists.add(list);
						list = null;
					}
					else if (reader.getLocalName().equalsIgnoreCase("lists")) {
						// any necessary finalization
					}
				}
			}

			reader.close();
			return lists;
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
