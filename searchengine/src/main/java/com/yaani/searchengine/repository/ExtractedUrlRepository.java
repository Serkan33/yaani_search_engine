package com.yaani.searchengine.repository;

import com.yaani.searchengine.entity.ExtractedUrl;
import com.yaani.searchengine.entity.SitemapInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExtractedUrlRepository extends JpaRepository<ExtractedUrl, Long> {
}
