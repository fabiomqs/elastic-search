package com.example.demo.service;

import com.example.demo.repository.GenreRepository;
import com.example.demo.repository.MovieRepository;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    public MovieService(
            MovieRepository movieRepository,
            GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
    }

    public void importMovies() {

    }
}
