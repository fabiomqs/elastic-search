package com.github.fabiomqs.repository;

import com.github.fabiomqs.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
