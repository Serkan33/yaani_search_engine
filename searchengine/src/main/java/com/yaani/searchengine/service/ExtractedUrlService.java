package com.yaani.searchengine.service;

import com.yaani.searchengine.dto.PayloadDto;
import com.yaani.searchengine.entity.ExtractedUrl;
import com.yaani.searchengine.entity.SitemapInfo;

import java.util.List;
import java.util.Set;

public interface ExtractedUrlService {

    public void save(PayloadDto payload);

    public List<ExtractedUrl> findAll();
    public void saveAll(List<ExtractedUrl> extractedUrls);
}
