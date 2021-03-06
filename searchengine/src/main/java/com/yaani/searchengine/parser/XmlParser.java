package com.yaani.searchengine.parser;

import com.yaani.searchengine.entity.ExtractedUrl;
import com.yaani.searchengine.entity.SitemapInfo;
import lombok.extern.slf4j.Slf4j;
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
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class XmlParser {

    public  List<ExtractedUrl> parse(String content, SitemapInfo sitemapInfo){
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
    public List<ExtractedUrl> parseWithStAX(String content,SitemapInfo sitemapInfo){

        List<ExtractedUrl> locations = new ArrayList<>();
        ExtractedUrl extractedUrl;
        try {
            byte [] xmlArray = content.getBytes(StandardCharsets.UTF_8);
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader streamReader = inputFactory.createXMLStreamReader(new ByteArrayInputStream(xmlArray));
            while (streamReader.hasNext()) {
                if (streamReader.isStartElement()) {
                    if ("loc".equals(streamReader.getLocalName())&&"".equals(streamReader.getPrefix())) {
                        String loc = streamReader.getElementText();
                        extractedUrl = new ExtractedUrl();
                        extractedUrl.setSitemapInfo(sitemapInfo);
                        extractedUrl.setCreatedDate(new Date());
                        extractedUrl.setLastModifiedDate(new Date());
                        extractedUrl.setUrl(loc);
                        locations.add(extractedUrl);
                    }
                }
                streamReader.next();
            }
        }
        catch (Exception e){
            log.error("Sitemap-Response-Content: "+content);
            throw  new RuntimeException(e.getMessage());
        }
        return locations;
    }

    public String parseWithStAXStr(String content){

        String data ="";
        int count = 0;
        try {
            byte [] xmlArray = content.getBytes(StandardCharsets.UTF_8);
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader streamReader = inputFactory.createXMLStreamReader(new ByteArrayInputStream(xmlArray));
            while (streamReader.hasNext()) {
                if (streamReader.isStartElement()) {
                    if ("loc".equals(streamReader.getLocalName())) {
                        data+="{\"url\": \""+streamReader.getElementText()+"\"},";
                        count++;
                    }
                }
                streamReader.next();
            }
        }
        catch (Exception e){
            throw  new RuntimeException(e.getMessage());
        }
         return  "["+data+"]";

    }
}
