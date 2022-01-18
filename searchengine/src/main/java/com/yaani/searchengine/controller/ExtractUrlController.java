package com.yaani.searchengine.controller;

import com.yaani.searchengine.dto.PayloadDto;
import com.yaani.searchengine.entity.ExtractedUrl;
import com.yaani.searchengine.entity.SitemapInfo;
import com.yaani.searchengine.parser.XmlParser;
import com.yaani.searchengine.service.impl.ExtractedUrlServiceImpl;
import com.yaani.searchengine.service.impl.SitemapServiceImpl;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@RestController
public class ExtractUrlController {

    private  final ExtractedUrlServiceImpl extractUrlService;
    private final SitemapServiceImpl sitemapService;
    private  final RestTemplate restTemplate;
    private  final XmlParser xmlParser;
    public ExtractUrlController(ExtractedUrlServiceImpl extractUrlService, SitemapServiceImpl sitemapService, RestTemplateBuilder restTemplateBuilder, XmlParser xmlParser) {
        this.extractUrlService = extractUrlService;
        this.sitemapService = sitemapService;
        this.restTemplate =  restTemplateBuilder.build();
        this.xmlParser = xmlParser;
    }

    @PostMapping("parse/xml")
    public String extractUrl(@RequestBody PayloadDto payload){
        long time = System.currentTimeMillis();
        String  content = this.restTemplate.getForObject(payload.getSitemapUrl(), String.class);
        URI uri = null;
        try {
            uri = new URI(payload.getSitemapUrl());
            SitemapInfo sitemapInfo = new SitemapInfo();
            String domain = uri.getHost();
            sitemapInfo.setDomain(domain);
            sitemapInfo.setUrl(payload.getSitemapUrl());
            sitemapInfo = sitemapService.save(sitemapInfo);
            List<ExtractedUrl> extractedUrls =  xmlParser.getLocations(content,sitemapInfo);
            extractUrlService.saveAll(extractedUrls);
            time = System.currentTimeMillis() - time;
            sitemapInfo.setProcessingTime(time);
            sitemapInfo.setUrlCount(extractedUrls.size());
            sitemapService.save(sitemapInfo);
        } catch (Exception e) {
            throw  new RuntimeException(e.getMessage());
        }
        return "Success";
    }

    @GetMapping("url/list")
    public List<ExtractedUrl> list(){
       return extractUrlService.findAll();
    }
    @GetMapping("stats")
    public  List<SitemapInfo> sitemapInfoList(){
        return sitemapService.findAll();
    }
}
