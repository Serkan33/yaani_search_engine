package com.yaani.searchengine.parser;

import com.yaani.searchengine.entity.ExtractedUrl;
import com.yaani.searchengine.entity.SitemapInfo;
import org.springframework.stereotype.Component;
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
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class XmlParser {

    public  List<ExtractedUrl> getLocations(String content, SitemapInfo sitemapInfo){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        List<ExtractedUrl> locations = new ArrayList<>();
        ExtractedUrl extractedUrl;
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
                    extractedUrl = new ExtractedUrl();
                    extractedUrl.setSitemapInfo(sitemapInfo);
                    extractedUrl.setUrl(loc);
                    locations.add(extractedUrl);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return locations;
    }
}
