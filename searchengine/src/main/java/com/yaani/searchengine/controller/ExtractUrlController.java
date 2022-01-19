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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    public SitemapInfo extractUrl(@RequestBody PayloadDto payload) throws ExecutionException, InterruptedException {
        CompletableFuture<SitemapInfo> sitemapInfoCompletableFuture = sitemapService.asyncParseUrl(payload);
        System.out.println("******* Async asyncParseUrl started *******");
        SitemapInfo sitemapInfo = sitemapInfoCompletableFuture.get();
        sitemapService.asyncSaveExtractedUrl(sitemapInfo.getExtractedUrls());
        System.out.println("asyncParseUrl Response");
        return sitemapInfo;
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
