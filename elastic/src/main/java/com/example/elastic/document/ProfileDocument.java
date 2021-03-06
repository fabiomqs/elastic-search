package com.example.elastic.document;

import com.example.elastic.model.Technologies;
import lombok.Data;

import java.util.List;

@Data
public class ProfileDocument {

    private String id;
    private String firstName;
    private String lastName;
    private List<Technologies> technologies;
    private Location location;
    private List<String> emails;

}
