package com.example.demo.service;

import com.example.demo.event.MovieEvent;
import com.example.demo.model.Genre;
import com.example.demo.model.Movie;
import com.example.demo.repository.GenreRepository;
import com.example.demo.repository.MovieRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public MovieService(
            MovieRepository movieRepository,
            GenreRepository genreRepository,
            ApplicationEventPublisher applicationEventPublisher) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void importMovies() throws FileNotFoundException {

        File file = ResourceUtils.getFile("classpath:csv/movies.csv");

        List<Movie> records = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        scanner.hasNextLine();
        while (scanner.hasNextLine()) {
            records.add(getRecordFromLine(scanner.nextLine()));
        }
        for(Movie movie : records) {
            addNewMovie(movie);
        }
    }

    @Transactional
    public void addGenre(Long idMovie, Long idGenre) throws Exception {
        Movie movie = movieRepository.findById(idMovie)
                .orElseThrow(() -> new Exception("Wrong idMovie"));
        Genre genre =  genreRepository.findById(idGenre)
                .orElseThrow(() -> new Exception("Wrong idGenre"));
        movie.getGenres().add(genre);
        movieRepository.save(movie);
        applicationEventPublisher
                .publishEvent(
                        MovieEvent.builder()
                                .id(idMovie)
                                .type("UPDATE")
                                .build());
    }

    public Movie getMovie(Long id) throws Exception {
        return movieRepository.findById(id)
                .orElseThrow(() -> new Exception("Wrong idMovie"));
    }

    @Transactional
    private void addNewMovie(Movie movie) {
        Movie savedMovie = movieRepository.save(movie);
        applicationEventPublisher
                .publishEvent(
                        MovieEvent.builder()
                                .id(savedMovie.getId())
                                .type("CREATE")
                                .build());
    }

    private Movie getRecordFromLine(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }

        List<Movie> movies = new ArrayList<>();
        Movie movie = Movie.builder()
                .movieId(values.get(0))
                .title(values.get(1))
                .build();
        Scanner rowScanner = new Scanner(values.get(2));
        rowScanner.useDelimiter("|");
        Set<Genre> genres = new HashSet<>();
        while (rowScanner.hasNext()) {
            Genre genre = getGenreByName(rowScanner.next());
            genre.getMovies().add(movie);
            genres.add(genre);
        }
        movie.setGenres(genres);

        return movie;
    }

    private Genre getGenreByName(String name) {
        return genreRepository
                .findByName(name)
                .orElse(
                        Genre.builder()
                                .name(name)
                                .build());
    }
}
