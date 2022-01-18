package com.yaani.searchengine.service.impl;

import com.yaani.searchengine.dto.PayloadDto;
import com.yaani.searchengine.entity.ExtractedUrl;
import com.yaani.searchengine.repository.ExtractedUrlRepository;
import com.yaani.searchengine.service.ExtractedUrlService;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ExtractedUrlServiceImpl implements ExtractedUrlService {

    private  final ExtractedUrlRepository extractUrlRepository;
    public ExtractedUrlServiceImpl(ExtractedUrlRepository extractUrlRepository){
        this.extractUrlRepository = extractUrlRepository;
    }


    @Override
    public void save(PayloadDto payload) {

    }

    @Override
    public List<ExtractedUrl> findAll() {
       return  extractUrlRepository.findAll();
    }

    @Override
    public void saveAll(List<ExtractedUrl> extractedUrls) {
        extractUrlRepository.saveAll(extractedUrls);
    }

}
