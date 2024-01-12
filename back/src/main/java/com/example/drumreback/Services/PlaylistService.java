package com.example.drumreback.Services;

import com.example.drumreback.Entities.Playlist;
import com.example.drumreback.Entities.PlaylistId;
import com.example.drumreback.Entities.SongInPlaylist;
import com.example.drumreback.Repositories.PlaylistRepository;
import com.example.drumreback.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class PlaylistService {
    private PlaylistRepository playlistRepository;

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

        Playlist playlist = new Playlist(playlistId, users, songs);

        playlistRepository.save(playlist);

        return playlist;
    }

    public void savePlaylist(Playlist playlist) {
        playlistRepository.save(playlist);
    }
}
