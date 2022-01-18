package com.yaani.searchengine.service.impl;

import com.yaani.searchengine.entity.ExtractedUrl;
import com.yaani.searchengine.entity.SitemapInfo;
import com.yaani.searchengine.repository.ExtractedUrlRepository;
import com.yaani.searchengine.repository.SitemapRepository;
import com.yaani.searchengine.service.SitemapService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class SitemapServiceImpl implements SitemapService {
    private  final SitemapRepository sitemapRepository;
    private final ExtractedUrlRepository extractedUrlRepository;

    public SitemapServiceImpl(SitemapRepository sitemapRepository, ExtractedUrlRepository extractedUrlRepository) {
        this.sitemapRepository = sitemapRepository;
        this.extractedUrlRepository = extractedUrlRepository;
    }

    @Override
    public void save() {
        SitemapInfo sitemapInfo = new SitemapInfo();
        sitemapInfo.setDomain("google.com");
        sitemapInfo.setUrlCount(2);
        sitemapInfo.setProcessingTime(100L);
        sitemapInfo.setId(UUID.randomUUID());

        SitemapInfo map =  sitemapRepository.save(sitemapInfo);
        ExtractedUrl extractedUrl = new ExtractedUrl();
        extractedUrl.setUrl("http://test.com");
        extractedUrl.setSitemapInfo(map);
        extractedUrl.setId(UUID.randomUUID());
        extractedUrlRepository.save(extractedUrl);
    }

    @Override
    public List<SitemapInfo> findAll() {
        return sitemapRepository.findAll();
    }
}
