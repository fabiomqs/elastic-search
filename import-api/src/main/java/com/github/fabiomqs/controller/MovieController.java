package com.github.fabiomqs.controller;

import com.github.fabiomqs.dto.SuccessResponse;
import com.github.fabiomqs.model.Movie;
import com.github.fabiomqs.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/import")
    public SuccessResponse importMovies() throws FileNotFoundException {
        movieService.importMovies();
        return SuccessResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message("ok")
                .build();
    }

    @PostMapping("/add/genre/{idMovie}/{idGenre}")
    public SuccessResponse addGenre(
            @PathVariable Long idMovie,
            @PathVariable Long idGenre) throws Exception {
        movieService.addGenre(idMovie, idGenre);
        return SuccessResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message("ok")
                .build();
    }

    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable Long id) throws Exception {
        return movieService.getMovie(id);
    }
}
