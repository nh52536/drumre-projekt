package com.example.drumreback.Services;

import com.example.drumreback.Entities.SongInPlaylist;
import com.example.drumreback.Entities.SongInPlaylistId;
import com.example.drumreback.Repositories.SongInPlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SongInPlaylistService {
    private SongInPlaylistRepository songInPlaylistRepository;

    @Autowired
    public SongInPlaylistService(SongInPlaylistRepository songInPlaylistRepository) {
        this.songInPlaylistRepository = songInPlaylistRepository;
    }

    public Optional<SongInPlaylist> getSongInPlaylist(SongInPlaylistId id) {
        return songInPlaylistRepository.findById(id);
    }

    public void save(SongInPlaylist sip) {
        songInPlaylistRepository.save(sip);
    }

    public void delete(SongInPlaylist sip) {
        Optional<SongInPlaylist> osip = songInPlaylistRepository.findById(sip.getSongInPlaylistId());
        if (osip.isPresent()) {
            songInPlaylistRepository.deleteById(sip.getSongInPlaylistId());
            System.out.println("Uspio sam izbrisati !!!");
        }
        else {
            System.out.println("Nisam uspio izbrisati!!!");
        }

    }
}
