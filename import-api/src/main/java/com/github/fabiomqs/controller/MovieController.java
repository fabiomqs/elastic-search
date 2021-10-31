package com.github.fabiomqs.controller;

import com.github.fabiomqs.dto.SuccessResponse;
import com.github.fabiomqs.model.Movie;
import com.github.fabiomqs.service.MovieService;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/import")
    public SuccessResponse importMovies() throws FileNotFoundException {
        movieService.importMovies();
        return SuccessResponse.create("OK");
    }

    @PostMapping("/add/genre/{idMovie}/{idGenre}")
    public SuccessResponse addGenre(
            @PathVariable Long idMovie,
            @PathVariable Long idGenre) throws Exception {
        movieService.addGenre(idMovie, idGenre);
        return SuccessResponse.create("OK");
    }

    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable Long id) throws Exception {
        return movieService.getMovie(id);
    }
}
