package com.github.fabiomqs.elastic.service;

import com.github.fabiomqs.elastic.model.Genre;
import com.github.fabiomqs.elastic.model.Movie;
import com.github.fabiomqs.elastic.repository.MovieRepository;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Service;
import org.elasticsearch.common.Strings;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public void createMovieIndexWithAnalizer() throws IOException {
        //Apenas Cria Indice, se já existe retorna erro
        CreateIndexResponse response =
                client.admin().indices().prepareCreate(INDEX_PREFIX + "movies").get(); //java_movies
        System.out.println("response " + response.isAcknowledged());

        // Adiciona Mapping ao indice já existente
        XContentBuilder builder = XContentFactory.jsonBuilder().
                startObject().startObject("properties")
                .startObject("id").field("type", "integer").endObject()
                .startObject("db_id").field("type", "integer").endObject()
                .startObject("year").field("type", "integer").endObject()
                .startObject("genre").field("type", "keyword").endObject()
                .startObject("title").field("type", "text").field("analyzer", "english").endObject()
                .endObject().endObject();
        AcknowledgedResponse response2 = client.admin().indices().preparePutMapping(INDEX_PREFIX + "movies").setType("mappings").setSource(builder).execute().actionGet();
        if (response2.isAcknowledged()) {
            System.out.println("java_movies mapping created !");
        } else {
            System.err.println("java_movies mapping creation failed !");
        }

        //Cria indece com mapping, não suporta objeto dentro de objeto!?
//        CreateIndexResponse response =
//                client.admin().indices().prepareCreate(INDEX_PREFIX + "movies") //java_movies
//
//                        .addMapping("properties",
//                                "id","type=integer",
//                                        "db_id","type=integer",
//                                        "year","type=integer",
//                                        "genre","type=keyword",
//                                        "title","type=text,analyzer=english")
//                        .get();


        //System.out.println("response " + response.isAcknowledged());
    }

    public void createIndexWithRelation() throws IOException {
        CreateIndexResponse response =
                client.admin().indices().prepareCreate(INDEX_PREFIX + "series").get(); //java_movies
        System.out.println("response " + response.isAcknowledged());

        XContentBuilder builder = XContentFactory.jsonBuilder().
                startObject().startObject("properties")
                .startObject("film_to_franchise")
                    .field("type", "join")
                .startObject("relations").field("franchise", "film").endObject()
                .endObject()
                .endObject().endObject();
        AcknowledgedResponse response2 = client.admin().indices().preparePutMapping(INDEX_PREFIX + "series").setType("mappings").setSource(builder).execute().actionGet();
        if (response2.isAcknowledged()) {
            System.out.println("java_movies mapping created !");
        } else {
            System.err.println("java_movies mapping creation failed !");
        }
    }

    public void buildJason() throws IOException {
        System.out.println(Strings.toString(jsonBuilder()
                .startObject()
                .startObject("id").field("type", "integer").endObject()
                .startObject("db_id").field("type", "integer").endObject()
                .startObject("year").field("type", "integer").endObject()
                .startObject("genre").field("type", "keyword").endObject()
                .startObject("title").field("type", "text").field("analyzer", "english").endObject()
                .endObject().prettyPrint()));
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
                        .field("year", movie.getIntYear())
                        .field("genre", genres)
                        .endObject()
                )
                .get();
        System.out.println("response id:"+response.getId());
        //System.out.println("title :" + movie.getTitle() + "size: " + genres.length);
        //return response.getResult().toString();
    }
}
