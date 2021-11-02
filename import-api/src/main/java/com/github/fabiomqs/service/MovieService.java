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
    public void importAllMovies() throws FileNotFoundException {

        File file = ResourceUtils.getFile("classpath:csv/movies.csv");

        List<Movie> records = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            records.add(getRecordFromLine2(scanner.nextLine()));
        }
        for(Movie movie : records) {
            System.out.println(movie.toString());
            //addNewMovie(movie);
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
        Scanner rowScanner = new Scanner(values.get(2));
        rowScanner.useDelimiter("\\|");
        Set<Genre> genres = new HashSet<>();
        while (rowScanner.hasNext()) {
            String strGenre = rowScanner.next();
            Genre genre = getGenreByName(strGenre);
            genre.addMovie(movie);
        }

        return movie;
    }

    private Movie getRecordFromLine2(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        if(values.size() > 3) {
            List<String> temp = new ArrayList<>();
            temp.add(values.get(0));
            String titleComplete = values.get(1);
            for(int i = 2; i <= values.size() -2;i++) {
                titleComplete += ", " + values.get(i);
            }
            temp.add(titleComplete);
            temp.add(values.get(values.size() -1));
            values = temp;
        }

        List<Movie> movies = new ArrayList<>();
        Movie movie = new Movie(values.get(0), values.get(1));
        if(!line.contains("no genres listed")) {
            Scanner rowScanner = new Scanner(values.get(2));
            rowScanner.useDelimiter("\\|");
            Set<Genre> genres = new HashSet<>();
            while (rowScanner.hasNext()) {
                String strGenre = rowScanner.next();
                Genre genre = new Genre(strGenre);
                //Genre genre = getGenreByName(strGenre);
                genre.addMovie(movie);
            }
        }

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
