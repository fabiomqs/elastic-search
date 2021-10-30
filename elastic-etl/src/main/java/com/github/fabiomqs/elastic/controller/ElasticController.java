package com.github.fabiomqs.elastic.controller;

import com.github.fabiomqs.elastic.dto.SuccessResponse;
import com.github.fabiomqs.elastic.service.ElasticService;
 import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/elastic")
public class ElasticController {

    private final ElasticService elasticService;

    public ElasticController(ElasticService elasticService) {
        this.elasticService = elasticService;
    }

    @GetMapping("/create/index/movie")
    public SuccessResponse createIndex() {
        elasticService.createMovieIndex();
        return SuccessResponse.create("OK");
    }
}
