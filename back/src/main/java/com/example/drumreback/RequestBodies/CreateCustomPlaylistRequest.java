package com.example.drumreback.RequestBodies;

import com.example.drumreback.Entities.PlaylistId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCustomPlaylistRequest {
    private String token;
    private PlaylistId playlistId;
    private String id;
}