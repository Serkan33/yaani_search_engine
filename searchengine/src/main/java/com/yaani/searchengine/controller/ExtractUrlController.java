package com.yaani.searchengine.controller;

import com.yaani.searchengine.dto.PayloadDto;
import com.yaani.searchengine.entity.ExtractedUrl;
import com.yaani.searchengine.entity.SitemapInfo;
import com.yaani.searchengine.exception.MissingRequiredFieldException;
import com.yaani.searchengine.service.impl.ExtractedUrlServiceImpl;
import com.yaani.searchengine.service.impl.SitemapServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

@RestController
@Slf4j
public class ExtractUrlController {

    private final ExtractedUrlServiceImpl extractUrlService;
    private final SitemapServiceImpl sitemapService;

    public ExtractUrlController(ExtractedUrlServiceImpl extractUrlService, SitemapServiceImpl sitemapService) {
        this.extractUrlService = extractUrlService;
        this.sitemapService = sitemapService;
    }

    @PostMapping("parse/xml")
    public SitemapInfo extractUrl(@Valid @RequestBody PayloadDto payload) throws ExecutionException, InterruptedException {
        String pattern = "((http|https)://)(www.)?[a-zA-Z0-9@:%._\\+~#?&//=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%._\\+~#?&//=]*)\\.xml$";
        if (!Pattern.matches(pattern,payload.getSitemapUrl())){
            log.error("Geçerli bir sitemap.xml adresi giriniz");
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"Geçerli bir sitemap.xml adresi giriniz");
        }
        CompletableFuture<SitemapInfo> sitemapInfoCompletableFuture = sitemapService.asyncParseUrl(payload);
        SitemapInfo sitemapInfo = sitemapInfoCompletableFuture.get();
        sitemapService.asyncSaveExtractedUrl(sitemapInfo.getExtractedUrls());
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
}
