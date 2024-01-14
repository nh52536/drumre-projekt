package com.example.drumreback.Services;

import com.example.drumreback.Entities.Song;
import com.example.drumreback.Repositories.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongService {
    private SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Song getSong(int songId) {return songRepository.findById(songId).get();}
}
