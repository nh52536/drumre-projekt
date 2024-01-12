package com.example.drumreback.Entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Song {
    @Id
    private Integer songId;
    private String author;
    private int popularity; // 0 - 100
}
