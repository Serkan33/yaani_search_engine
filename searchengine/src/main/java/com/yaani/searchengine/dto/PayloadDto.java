package com.yaani.searchengine.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayloadDto {

    @NotNull
    private  String sitemapUrl;
}
