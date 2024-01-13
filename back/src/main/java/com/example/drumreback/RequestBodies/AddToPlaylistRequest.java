package com.example.drumreback.RequestBodies;

import com.example.drumreback.Entities.PlaylistId;
import com.example.drumreback.Entities.Song;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToPlaylistRequest {
    private Song song;
    private String username;
    private PlaylistId playlistId;
}
