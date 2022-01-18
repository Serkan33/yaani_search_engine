package com.yaani.searchengine.service.impl;

import com.yaani.searchengine.dto.PayloadDto;
import com.yaani.searchengine.entity.ExtractedUrl;
import com.yaani.searchengine.entity.SitemapInfo;
import com.yaani.searchengine.parser.XmlParser;
import com.yaani.searchengine.repository.ExtractedUrlRepository;
import com.yaani.searchengine.repository.SitemapRepository;
import com.yaani.searchengine.service.SitemapService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Service
public class SitemapServiceImpl implements SitemapService {
    private  final SitemapRepository sitemapRepository;
    public SitemapServiceImpl(SitemapRepository sitemapRepository) {
        this.sitemapRepository = sitemapRepository;
    }

    @Override
    public SitemapInfo save(SitemapInfo sitemapInfo) {
        return  sitemapRepository.save(sitemapInfo);
    }

    @Override
    public List<SitemapInfo> findAll() {
        return sitemapRepository.findAll();
    }
}
