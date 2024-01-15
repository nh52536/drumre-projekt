package com.example.drumreback.Services;

import com.example.drumreback.Entities.SongInPlaylist;
import com.example.drumreback.Entities.SongInPlaylistId;
import com.example.drumreback.Repositories.SongInPlaylistRepository;
import com.example.drumreback.RequestBodies.RecommendSongsRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SongInPlaylistService {
    private SongInPlaylistRepository songInPlaylistRepository;

    @Autowired
    public SongInPlaylistService(SongInPlaylistRepository songInPlaylistRepository) {
        this.songInPlaylistRepository = songInPlaylistRepository;
    }

    public Optional<SongInPlaylist> getSongInPlaylist(SongInPlaylistId id) {
        return songInPlaylistRepository.findById(id);
    }

    public void save(SongInPlaylist sip) {
        songInPlaylistRepository.save(sip);
    }

    public void delete(SongInPlaylist sip) {
        Optional<SongInPlaylist> osip = songInPlaylistRepository.findById(sip.getSongInPlaylistId());
        if (osip.isPresent()) {
            songInPlaylistRepository.deleteById(sip.getSongInPlaylistId());
        }
    }

    public List<String> recommendSongs(String token, String[] genres, int min_popularity, int max_popularity) throws URISyntaxException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        String allGenres = "";
        for (int i = 0; i < genres.length; i++) {
            allGenres += genres[i];
            if (i + 1 < genres.length) allGenres += "%2C";
        }

        System.out.println(allGenres);

        URI uri = new URI("https://api.spotify.com/v1/recommendations?limit=50&market=HR&seed_genres=" + allGenres + "&min_popularity=" + String.valueOf(min_popularity) + "&max_popularity=" + String.valueOf(max_popularity));

        System.out.println(uri);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);

        System.out.println(response);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(response.getBody());

        List<String> recommendedSongs = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            recommendedSongs.add(responseJson.path("tracks").get(i).path("id").asText());
            System.out.println(responseJson.path("tracks").get(i).path("id").asText());
        }
        return recommendedSongs;
    }
}
