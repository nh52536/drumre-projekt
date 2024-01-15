package com.example.drumreback.Services;

import com.example.drumreback.Entities.Playlist;
import com.example.drumreback.Entities.PlaylistId;
import com.example.drumreback.Entities.SongInPlaylist;
import com.example.drumreback.Repositories.PlaylistRepository;
import com.example.drumreback.Repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.management.PlatformLoggingMXBean;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {
    private PlaylistRepository playlistRepository;

    @Autowired
    SongInPlaylistService songInPlaylistService;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public Optional<Playlist> findPlaylistById(PlaylistId playlistId) {
        return playlistRepository.findById(playlistId);
    }

    public Playlist addPlaylist(PlaylistId playlistId) {
        ArrayList<String> users = new ArrayList<>();
        users.add(playlistId.getCreatorUsername());
        ArrayList<SongInPlaylist> songs = new ArrayList<>();
        String uri = "";

        Playlist playlist = new Playlist(playlistId, users, songs, uri);

        playlistRepository.save(playlist);

        return playlist;
    }

    public void savePlaylist(Playlist playlist) {
        playlistRepository.save(playlist);
    }

    public void delete(PlaylistId id) { playlistRepository.deleteById(id); }

    public String createCustomPlaylist(String token, PlaylistId playlistId, String userId) throws URISyntaxException, JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");

        String body = "{\n" +
                "    \"name\" : \"" + playlistId.getPlaylistName() + "\",\n" +
                "    \"description\": \"Playlist generated by DruMre app\",\n" +
                "    \"public\": true\n" +
                "}";

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        URI uri = new URI("https://api.spotify.com/v1/users/" + userId + "/playlists");

        String response = restTemplate.postForObject(uri, request, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(response);

        addItemsToPlaylist(token, playlistId, responseJson.path("id").asText());

        Playlist playlist = playlistRepository.findById(playlistId).get();
        playlist.setUri(responseJson.path("external_urls").path("spotify").asText());
        playlistRepository.save(playlist);
        return responseJson.path("external_urls").path("spotify").asText();
    }

    public String addItemsToPlaylist(String token, PlaylistId playlistId, String playlistSpotifyId) throws URISyntaxException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");

        Playlist playlist = playlistRepository.findById(playlistId).get();
        List<SongInPlaylist> lista = playlist.getSongs();
        int tracks = lista.size();

        System.out.println(" /////// ");

        String body = "{\n" +
                "    \"uris\" : [\n";
        for (int i = 0; i < tracks; i++) {
            body += "        \"spotify:track:";
            body += lista.get(i).getSong().getSongId();
            body += "\"";
            if (i + 1 < tracks) body += ",";
            body += "\n";
        }
        body += "    ],\n";
        body += "    \"position\" : 0\n";
        body += "}";

        System.out.println(body);
        System.out.println(" /////// ");

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        URI uri = new URI("https://api.spotify.com/v1/playlists/" + playlistSpotifyId + "/tracks");

        return restTemplate.postForObject(uri, request, String.class);
    }
}
