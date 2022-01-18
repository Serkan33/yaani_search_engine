package com.yaani.searchengine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SitemapInfo extends BaseEntity{

    private String domain;
    private Integer urlCount;
    private Long processingTime;
    @OneToMany(mappedBy = "sitemapInfo",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ExtractedUrl> extractedUrls;
}
