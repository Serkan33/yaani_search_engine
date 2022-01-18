package com.yaani.searchengine.service;

import com.yaani.searchengine.dto.PayloadDto;
import com.yaani.searchengine.entity.SitemapInfo;
import com.yaani.searchengine.service.impl.SitemapServiceImpl;

import java.util.List;

public interface SitemapService {

    public SitemapInfo save(SitemapInfo sitemapInfo);
    public List<SitemapInfo> findAll();
}
