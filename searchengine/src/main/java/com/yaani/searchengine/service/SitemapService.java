package com.yaani.searchengine.service;

import com.yaani.searchengine.entity.SitemapInfo;

import java.util.List;

public interface SitemapService {

    public SitemapInfo save(SitemapInfo sitemapInfo);
    public List<SitemapInfo> findAll();
}
