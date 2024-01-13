package com.example.drumreback.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class SongInPlaylistId {
    private String playlistName;
    private String playlistCreator;
    private String songId;

    @Override
    public int hashCode() {
        return Objects.hash(playlistName, playlistCreator, songId);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SongInPlaylistId)) return false;

        SongInPlaylistId songInPlaylistId = (SongInPlaylistId) obj;

        if (this.playlistName.equals(songInPlaylistId.playlistName) &&
                this.playlistCreator.equals(songInPlaylistId.playlistCreator) &&
                this.songId == songInPlaylistId.songId) return true;

        return false;
    }
}
