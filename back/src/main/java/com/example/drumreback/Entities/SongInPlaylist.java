package com.example.drumreback.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@AllArgsConstructor
public class SongInPlaylist {
    @Id
    private SongInPlaylistId songInPlaylistId;
    private Song song;
    private List<String> addedBy;
}
