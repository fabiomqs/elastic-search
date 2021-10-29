package com.example.demo.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String movieId;

    private String title;

    @Singular
    @ManyToMany(mappedBy = "movies")
    private Set<Genre> genres;
}
