package com.example.drumreback.Entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class User {
    @Id
    private String username;
    private List<String> createdPlaylistsNames;
    private List<PlaylistId> inPlaylists;
}
