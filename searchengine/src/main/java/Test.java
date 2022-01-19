import com.fasterxml.aalto.AsyncInputFeeder;
import com.fasterxml.aalto.AsyncXMLStreamReader;
import com.fasterxml.aalto.stax.InputFactoryImpl;
import com.google.gson.Gson;
import com.yaani.searchengine.entity.ExtractedUrl;
import com.yaani.searchengine.entity.SitemapInfo;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws XMLStreamException {
        String json = "{"+"\"id\": \"dbc3e2f2-c945-4b6e-9ff5-102fa4bf3f03\","
                + "\"url\": \"https://www.haberturk.com/sitemap_headlines.xml\","
                + "\"createdDate\":\"2022-01-19\","
                + "\"lastModifiedDate\": \"2022-01-19\","
                + "\"domain\":\"www.haberturk.com\","
                + "\"urlCount\": 1206,"
                + "\"processingTime\": 8295,"
                + "\"extractedUrls\":[{\"url\": \"https://www.haberturk.com/18-ocak-2022-haberleri\"},{\"url\": \"https://www.haberturk.com/18-ocak-2022-haberleri\"}]}";
        SitemapInfo sitemapInfo = new SitemapInfo();
        SitemapInfo map = new Gson().fromJson(json,SitemapInfo.class);
        String url = "https://www.haberturk.com/sitemap_headlines.xml";
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();
        String content = restTemplate.getForObject(url, String.class);
        long time = System.currentTimeMillis();
        byte [] xml = content.getBytes(StandardCharsets.UTF_8);
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(new ByteArrayInputStream(xml));
        int count = 0;
        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();

            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();

                if (startElement.getName().getLocalPart().equals("loc")&&startElement.getName().getPrefix().equals("")) {

                    event = eventReader.nextEvent();

                    StringBuilder fName = new StringBuilder();

                    while (!event.isEndElement()) {
                        fName.append(event.asCharacters().getData());
                        event = eventReader.nextEvent();
                    }
                    count++;
                }

            }
        }
        time = System.currentTimeMillis() - time;

        System.out.println("Time1: "+time+" Count1: "+count);

        count =0;
        time = System.currentTimeMillis();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(content)));
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("url");
            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String loc = element.getElementsByTagName("loc").item(0).getTextContent();
                    count++;
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        time = System.currentTimeMillis() - time;

        System.out.println("Time2: "+time+" Count2: "+count);

        time = System.currentTimeMillis();

        count =0;

        time = System.currentTimeMillis() - time;

        System.out.println("Time3: "+time+" Count3: "+count);

    }
}
