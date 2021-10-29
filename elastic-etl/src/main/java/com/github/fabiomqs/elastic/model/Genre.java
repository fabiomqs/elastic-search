package com.github.fabiomqs.elastic.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Singular
    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies;

}
