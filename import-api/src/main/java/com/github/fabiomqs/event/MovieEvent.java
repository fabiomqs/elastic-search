package com.github.fabiomqs.event;

import com.github.fabiomqs.model.Movie;

public class MovieEvent {

    private Long id;
    private String type;

    public MovieEvent() {
    }

    public MovieEvent(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
