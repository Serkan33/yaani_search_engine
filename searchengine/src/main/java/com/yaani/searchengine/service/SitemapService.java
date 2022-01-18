package com.yaani.searchengine.service;

import com.yaani.searchengine.entity.SitemapInfo;

import java.util.List;

public interface SitemapService {

    public  void  save();
    public List<SitemapInfo> findAll();
}
