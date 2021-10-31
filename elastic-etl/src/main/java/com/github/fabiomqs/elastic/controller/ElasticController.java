package com.github.fabiomqs.elastic.controller;

import com.github.fabiomqs.elastic.dto.SuccessResponse;
import com.github.fabiomqs.elastic.service.ElasticService;
 import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/elastic")
public class ElasticController {

    private final ElasticService elasticService;

    public ElasticController(ElasticService elasticService) {
        this.elasticService = elasticService;
    }

    @GetMapping("/json")
    public SuccessResponse json() throws IOException {
        elasticService.buildJason();
        return SuccessResponse.create("OK");
    }

    @GetMapping("/index")
    public SuccessResponse createIndex() {
        elasticService.createMovieIndex();
        return SuccessResponse.create("OK");
    }

    @GetMapping("/analyzer")
    public SuccessResponse createIndexAnalizer() throws IOException {
        elasticService.createMovieIndexWithAnalizer();
        return SuccessResponse.create("OK");
    }

    @GetMapping("/relation")
    public SuccessResponse createIndexRelation() throws IOException {
        elasticService.createIndexWithRelation();
        return SuccessResponse.create("OK");
    }

}
