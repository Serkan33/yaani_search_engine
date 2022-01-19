package com.yaani.searchengine.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yaani.searchengine.dto.PayloadDto;
import com.yaani.searchengine.entity.ExtractedUrl;
import com.yaani.searchengine.entity.SitemapInfo;
import com.yaani.searchengine.parser.XmlParser;
import com.yaani.searchengine.service.impl.ExtractedUrlServiceImpl;
import com.yaani.searchengine.service.impl.SitemapServiceImpl;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.*;

@RestController
public class ExtractUrlController {

    private final ExtractedUrlServiceImpl extractUrlService;
    private final SitemapServiceImpl sitemapService;
    private final RestTemplate restTemplate;
    private final XmlParser xmlParser;

    public ExtractUrlController(ExtractedUrlServiceImpl extractUrlService, SitemapServiceImpl sitemapService, RestTemplateBuilder restTemplateBuilder, XmlParser xmlParser) {
        this.extractUrlService = extractUrlService;
        this.sitemapService = sitemapService;
        this.restTemplate = restTemplateBuilder.build();
        this.xmlParser = xmlParser;
    }

    @PostMapping("parse/xml")
    public String extractUrl(@RequestBody PayloadDto payload) {
        String content = this.restTemplate.getForObject(payload.getSitemapUrl(), String.class);
        long time = System.currentTimeMillis();
        URI uri = null;
        try {
            uri = new URI(payload.getSitemapUrl());
            SitemapInfo sitemapInfo = new SitemapInfo();
            String domain = uri.getHost();
            sitemapInfo.setDomain(domain);
            sitemapInfo.setSiteMapUrl(payload.getSitemapUrl());
            sitemapInfo = sitemapService.save(sitemapInfo);
            List<ExtractedUrl> extractedUrls = xmlParser.parseWithStAX(content, sitemapInfo);
            extractUrlService.saveAll(extractedUrls);
            time = System.currentTimeMillis() - time;
            sitemapInfo.setProcessingTime(time);
            sitemapInfo.setUrlCount(extractedUrls.size());
            sitemapService.save(sitemapInfo);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return "Success";
    }

    @GetMapping("url/list")
    public List<ExtractedUrl> list() {
        return extractUrlService.findAll();
    }

    @GetMapping("stats")
    public List<SitemapInfo> sitemapInfoList() {
        return sitemapService.findAll();
    }

    @PostMapping("statsList")
    public SitemapInfo statsList(@RequestBody PayloadDto payload) {
        Gson gson = new Gson();
        String content = this.restTemplate.getForObject(payload.getSitemapUrl(), String.class);
        long time = System.currentTimeMillis();
        String data = xmlParser.parseWithStAXStr(content);
        time = System.currentTimeMillis() - time;
        Type listType = new TypeToken<List<ExtractedUrl>>() {
        }.getType();
        List<ExtractedUrl> extractedUrls = gson.fromJson(data, listType);
        SitemapInfo sitemapInfo = new SitemapInfo();
        sitemapInfo.setExtractedUrls(extractedUrls);
        sitemapInfo.setProcessingTime(time);
        sitemapInfo.setUrlCount(extractedUrls.size());
        return sitemapInfo;

    }
}
