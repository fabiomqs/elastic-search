package com.github.fabiomqs.service;

import com.github.fabiomqs.event.MovieEvent;
import com.github.fabiomqs.model.Genre;
import com.github.fabiomqs.model.Movie;
import com.github.fabiomqs.repository.GenreRepository;
import com.github.fabiomqs.repository.MovieRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

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

    @Transactional
    public void importMovies() throws FileNotFoundException {

        File file = ResourceUtils.getFile("classpath:csv/movies_50.csv");

        List<Movie> records = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        scanner.nextLine();
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
                .publishEvent(new MovieEvent(idMovie, "UPDATE"));
    }

    public Movie getMovie(Long id) throws Exception {
        return movieRepository.findById(id)
                .orElseThrow(() -> new Exception("Wrong idMovie"));
    }


    private void addNewMovie(Movie movie) {
        Movie savedMovie = movieRepository.save(movie);
        applicationEventPublisher
                .publishEvent(new MovieEvent(savedMovie.getId(), "CREATE"));
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
        Movie movie = new Movie(values.get(0), values.get(1));

    //    String strGenres = values.get(2);
    //    for(String strGenre : strGenres.split("\\|")) {
    //        Genre genre = getGenreByName(strGenre);
    //        genre.addMovie(movie);
    //    }

        Scanner rowScanner = new Scanner(values.get(2));
        rowScanner.useDelimiter("\\|");
        Set<Genre> genres = new HashSet<>();
        while (rowScanner.hasNext()) {
            String strGenre = rowScanner.next();
            Genre genre = getGenreByName(strGenre);
            genre.addMovie(movie);
        }
        //movie.setGenres(genres);

        return movie;
    }

    private Genre getGenreByName(String name) {
        Genre genre = genreRepository.findByName(name);
        if(genre != null)
            return genre;
        Genre newGenre = new Genre(name);
        Genre savedGenre = genreRepository.save(newGenre);
        return savedGenre;
    }
}
