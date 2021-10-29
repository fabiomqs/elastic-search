package com.github.fabiomqs.elastic.repository;

import com.github.fabiomqs.elastic.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
