package com.github.fabiomqs.elastic.service;

import com.github.fabiomqs.elastic.model.Genre;
import com.github.fabiomqs.elastic.model.Movie;
import com.github.fabiomqs.elastic.repository.MovieRepository;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
public class ElasticService {

    public static final String INDEX_PREFIX = "java_";

    private final Client client;

    private final MovieRepository movieRepository;


    public ElasticService(Client client,
                          MovieRepository movieRepository) {
        this.client = client;
        this.movieRepository = movieRepository;
    }

    public void createMovieIndex() {
        CreateIndexResponse response =
                client.admin().indices().prepareCreate(INDEX_PREFIX + "movies") //java_movies
                        .addMapping("properties","year","type=date")
                .get();
        System.out.println("response " + response.isAcknowledged());
    }

    public void indexMovie(long idMovie) throws Exception {
        Movie movie = movieRepository.findById(idMovie)
                .orElseThrow(() -> new Exception("Movie no found"));


        String[] genres = new String[movie.getGenres().size()];
        int i = 0;
        for(Genre genre : movie.getGenres()) {
            genres[i++] = genre.getName();
        }

        IndexResponse response = client.prepareIndex(INDEX_PREFIX + "movies", "_doc", movie.getMovieId())
                .setSource(jsonBuilder()
                        .startObject()
                        .field("db_id", movie.getId())
                        .field("title", movie.getTitle())
                        .field("year", LocalDate.of( movie.getIntYear() , 1 , 1 ))
                        .field("genre", genres)
                        .endObject()
                )
                .get();
        System.out.println("response id:"+response.getId());
        //System.out.println("title :" + movie.getTitle() + "size: " + genres.length);
        //return response.getResult().toString();
    }
}
