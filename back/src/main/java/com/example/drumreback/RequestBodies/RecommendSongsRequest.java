package com.example.drumreback.RequestBodies;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendSongsRequest {
    private String token;
    private String[] genres;
    private int min_popularity;
    private int max_popularity;
}
