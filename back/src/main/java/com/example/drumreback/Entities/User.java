package com.example.drumreback.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@AllArgsConstructor
public class User {
    @Id
    private String username;
    private List<String> createdPlaylistsNames;
    private List<PlaylistId> inPlaylists;
}
