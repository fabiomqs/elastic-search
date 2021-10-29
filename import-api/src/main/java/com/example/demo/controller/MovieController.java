package com.example.demo.controller;

import com.example.demo.dto.SuccessResponse;
import com.example.demo.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/import")
    public SuccessResponse importMovies() {
        movieService.importMovies();
        return SuccessResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message("ok")
                .build();
    }
}
