package com.example.drumreback.RequestBodies;

import com.example.drumreback.Entities.PlaylistId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetLikedSongsRequest {
    private String username;
    private PlaylistId playlistId;
}
