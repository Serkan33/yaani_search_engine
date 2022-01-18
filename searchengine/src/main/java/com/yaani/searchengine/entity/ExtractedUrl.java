package com.yaani.searchengine.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ExtractedUrl extends BaseEntity {

    private String url;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable=false)
    @JsonIgnore
    private SitemapInfo sitemapInfo;

}
