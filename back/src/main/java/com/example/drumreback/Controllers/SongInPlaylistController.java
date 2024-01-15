package com.example.drumreback.Controllers;

import com.example.drumreback.RequestBodies.RecommendSongsRequest;
import com.example.drumreback.Services.SongInPlaylistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;

@RestController
public class SongInPlaylistController {
    private SongInPlaylistService songInPlaylistService;

    @Autowired
    public SongInPlaylistController(SongInPlaylistService songInPlaylistService) {
        this.songInPlaylistService = songInPlaylistService;
    }




    @PostMapping(path = "/recommendSongs",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<String>> recommendSongs(@RequestBody RecommendSongsRequest request) throws URISyntaxException, JsonProcessingException {
        List<String> recommendedSongs = songInPlaylistService.recommendSongs(request.getToken(), request.getGenres(), request.getMin_popularity(), request.getMax_popularity());

        return new ResponseEntity<>(recommendedSongs, HttpStatus.OK);
    }
}










