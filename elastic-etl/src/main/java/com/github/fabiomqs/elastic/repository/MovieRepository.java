package com.github.fabiomqs.elastic.repository;

import com.github.fabiomqs.elastic.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
