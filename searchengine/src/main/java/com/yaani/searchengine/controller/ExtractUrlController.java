package com.yaani.searchengine.controller;

import com.yaani.searchengine.dto.PayloadDto;
import com.yaani.searchengine.entity.ExtractedUrl;
import com.yaani.searchengine.service.impl.ExtractedUrlServiceImpl;
import com.yaani.searchengine.service.impl.SitemapServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class ExtractUrlController {

    private  final ExtractedUrlServiceImpl extractUrlService;
    public ExtractUrlController(ExtractedUrlServiceImpl extractUrlService) {
        this.extractUrlService = extractUrlService;
    }

    @PostMapping("parse/xml")
    public String extractUrl(@RequestBody PayloadDto payload){
        extractUrlService.save(payload);
        return "Success";
    }

    @GetMapping("url/list")
    public List<ExtractedUrl> list(){
       return extractUrlService.findAll();
    }
}
