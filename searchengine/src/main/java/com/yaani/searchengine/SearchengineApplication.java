package com.yaani.searchengine;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Yaani Search API", version = "1.0", description = "Sitemap.xml parse işlemi"))
public class SearchengineApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchengineApplication.class, args);
	}

}
