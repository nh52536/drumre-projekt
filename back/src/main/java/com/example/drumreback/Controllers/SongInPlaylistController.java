package com.example.drumreback.Controllers;

import com.example.drumreback.Services.SongInPlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SongInPlaylistController {
    private SongInPlaylistService songInPlaylistService;

    @Autowired
    public SongInPlaylistController(SongInPlaylistService songInPlaylistService) {
        this.songInPlaylistService = songInPlaylistService;
    }
}
