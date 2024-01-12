package com.example.drumreback.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class PlaylistId {
    private String creatorUsername;
    private String playlistName;

    @Override
    public int hashCode() {
        return Objects.hash(creatorUsername, playlistName);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlaylistId)) return false;

        PlaylistId playlistId = (PlaylistId) obj;

        if (this.creatorUsername.equals(playlistId.creatorUsername) &&
            this.playlistName.equals(playlistId.playlistName)) return true;

        return false;
    }
}
