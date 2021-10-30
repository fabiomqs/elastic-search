package com.github.fabiomqs.dto;

import org.springframework.http.HttpStatus;

public class SuccessResponse {

    private Integer status;
    private String message;

    public SuccessResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static SuccessResponse create(String message) {
        return new SuccessResponse(HttpStatus.OK.value(), message);
    }


}
