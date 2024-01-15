package com.example.drumreback.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@AllArgsConstructor
public class Playlist {
    @Id
    private PlaylistId playlistId;
    private List<String> users;
    private List<SongInPlaylist> songs;
    private String uri;
}
