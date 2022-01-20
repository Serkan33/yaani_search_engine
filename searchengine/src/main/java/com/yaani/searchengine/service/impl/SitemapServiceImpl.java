package com.yaani.searchengine.service.impl;

import com.yaani.searchengine.dto.PayloadDto;
import com.yaani.searchengine.entity.ExtractedUrl;
import com.yaani.searchengine.entity.SitemapInfo;
import com.yaani.searchengine.parser.XmlParser;
import com.yaani.searchengine.repository.ExtractedUrlRepository;
import com.yaani.searchengine.repository.SitemapRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
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
        log.info("******* Async asyncParseUrl started *******");
        String sitemapUrl = payloadDto.getSitemapUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.TEXT_XML_VALUE));
        HttpEntity<String> request =  new HttpEntity<>(headers);
        String content = this.restTemplate.getForObject(sitemapUrl,String.class,request);
        long time = System.currentTimeMillis();
        try {
            URI  uri = new URI(sitemapUrl);
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
            log.info("********** asyncParseUrl Response ***********");
             return  CompletableFuture.completedFuture(sitemapInfo);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage()+" , Response: "+content,e);
        }
    }

    @Async("asyncExecutor")
    public void asyncSaveExtractedUrl(List<ExtractedUrl> extractedUrls){
        log.info("*******START Async asyncSaveExtractedUrl *******");
        extractedUrlRepository.saveAll(extractedUrls);
        log.info("*******END Async asyncSaveExtractedUrl *******");
    }
}
