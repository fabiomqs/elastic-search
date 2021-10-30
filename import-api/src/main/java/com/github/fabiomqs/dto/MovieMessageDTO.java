package com.github.fabiomqs.dto;

public class MovieMessageDTO {

    private String idMovie;
    private String message;

    public MovieMessageDTO(String idMovie, String message) {
        this.idMovie = idMovie;
        this.message = message;
    }

    public String getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(String idMovie) {
        this.idMovie = idMovie;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
