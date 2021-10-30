package com.github.fabiomqs.elastic.ampq.dto;

public class MovieMessageDTO {

    private String idMovie;

    private String message;

    public MovieMessageDTO() {
    }

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

    public Long getLongIdMovie() {
        return Long.parseLong(this.idMovie);
    }
}
