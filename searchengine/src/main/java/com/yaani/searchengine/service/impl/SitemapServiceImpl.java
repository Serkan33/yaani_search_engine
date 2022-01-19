package com.yaani.searchengine.service.impl;

import com.yaani.searchengine.dto.PayloadDto;
import com.yaani.searchengine.entity.ExtractedUrl;
import com.yaani.searchengine.entity.SitemapInfo;
import com.yaani.searchengine.parser.XmlParser;
import com.yaani.searchengine.repository.ExtractedUrlRepository;
import com.yaani.searchengine.repository.SitemapRepository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class SitemapServiceImpl {
    private  final SitemapRepository sitemapRepository;
    private  final ExtractedUrlRepository extractedUrlRepository;
    private final RestTemplate restTemplate;
    private final XmlParser xmlParser;
    public SitemapServiceImpl(SitemapRepository sitemapRepository, ExtractedUrlRepository extractedUrlRepository, RestTemplateBuilder builder, XmlParser xmlParser) {
        this.sitemapRepository = sitemapRepository;
        this.extractedUrlRepository = extractedUrlRepository;
        this.restTemplate = builder.build();
        this.xmlParser = xmlParser;
    }

    public SitemapInfo save(SitemapInfo sitemapInfo) {
        return  sitemapRepository.save(sitemapInfo);
    }

    public List<SitemapInfo> findAll() {
        return sitemapRepository.findAll();
    }

    @Async("asyncExecutor")
    public CompletableFuture<SitemapInfo> asyncParseUrl(PayloadDto payloadDto) {

        String sitemapUrl = payloadDto.getSitemapUrl();
        String content = this.restTemplate.getForObject(sitemapUrl, String.class);
        long time = System.currentTimeMillis();
        URI uri = null;
        try {
            uri = new URI(sitemapUrl);
            SitemapInfo sitemapInfo = new SitemapInfo();
            String domain = uri.getHost();
            sitemapInfo.setDomain(domain);
            sitemapInfo.setSiteMapUrl(sitemapUrl);
            List<ExtractedUrl> extractedUrls = xmlParser.parseWithStAX(content, sitemapInfo);
            time = System.currentTimeMillis() - time;
            sitemapInfo.setProcessingTime(time);
            sitemapInfo.setUrlCount(extractedUrls.size());
            sitemapInfo =  sitemapRepository.save(sitemapInfo);
            sitemapInfo.setExtractedUrls(extractedUrls);
             return  CompletableFuture.completedFuture(sitemapInfo);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Async("asyncExecutor")
    public  CompletableFuture<List<ExtractedUrl>> asyncSaveExtractedUrl(List<ExtractedUrl> extractedUrls){
        System.out.println("*******START Async asyncSaveExtractedUrl *******");
        List<ExtractedUrl>  urls = extractedUrlRepository.saveAll(extractedUrls);
        System.out.println("*******END Async asyncSaveExtractedUrl *******");
        return  CompletableFuture.completedFuture(urls);
    }
}
