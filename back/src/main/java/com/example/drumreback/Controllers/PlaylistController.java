package com.example.drumreback.Controllers;

import com.example.drumreback.Entities.*;
import com.example.drumreback.RequestBodies.CreatePlaylistRequest;
import com.example.drumreback.Services.PlaylistService;
import com.example.drumreback.Services.SongInPlaylistService;
import com.example.drumreback.Services.SongService;
import com.example.drumreback.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Handler;

@RestController
public class PlaylistController {
    @Autowired
    UserService userService;

    @Autowired
    SongInPlaylistService songInPlaylistService;

    @Autowired
    SongService songService;

    private PlaylistService playlistService;

    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }



    @PostMapping(path = "/join",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> join(@RequestBody PlaylistId playlistId, @RequestParam String username) {
        Optional<Playlist> optionalPlaylist = playlistService.findPlaylistById(playlistId);

        if (optionalPlaylist.isPresent()) {
            userService.addUserToPlaylist(userService.findUserById(username).get(), playlistService.findPlaylistById(playlistId).get());
            User user = userService.findUserById(username).get();
            user.getInPlaylists().add(playlistId);
            userService.save(user);

            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }



    @PostMapping(path = "/createPlaylist",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> createPlaylist(@RequestBody CreatePlaylistRequest request, @RequestParam String username) {
        PlaylistId playlistId = new PlaylistId(username, request.getPlaylistName());
        if (playlistService.findPlaylistById(playlistId).isPresent())
            return new ResponseEntity<>(false, HttpStatus.OK);

        Playlist playlist = playlistService.addPlaylist(playlistId);

        User user = userService.findUserById(username).get();
        user.getInPlaylists().add(playlistId);
        user.getCreatedPlaylistsNames().add(request.getPlaylistName());
        userService.save(user);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }



    @PostMapping(path = "/addToPlaylist",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> addToPlaylist(@RequestBody Song song, @RequestParam String username, @RequestParam String playlistName, @RequestParam String creator) {
        PlaylistId playlistId = new PlaylistId(creator, playlistName);
        Playlist playlist = playlistService.findPlaylistById(playlistId).get();

        SongInPlaylistId id = new SongInPlaylistId(playlistName, creator, song.getSongId());
        if (songInPlaylistService.getSongInPlaylist(id).isPresent()) {
            SongInPlaylist SIP = songInPlaylistService.getSongInPlaylist(id).get();
            SIP.getAddedBy().add(username);
            songInPlaylistService.save(SIP);
            playlistService.savePlaylist(playlist);

            System.out.println(SIP.getAddedBy().size());
        }
        else {
            List<String> addedBy = new ArrayList<>();
            addedBy.add(username);
            SongInPlaylist songInPlaylist = new SongInPlaylist(id, song, addedBy);
            songInPlaylistService.save(songInPlaylist);

            playlist.getSongs().add(songInPlaylist);
            playlistService.savePlaylist(playlist);
        }

        return new ResponseEntity<>(true, HttpStatus.OK);
    }



    @DeleteMapping(path = "/deleteFromPlaylist")
    public ResponseEntity<Boolean> deleteFromPlaylist(@RequestParam String username, @RequestParam String playlistName, @RequestParam String creator, @RequestParam int songId) {
        SongInPlaylistId sipID = new SongInPlaylistId(playlistName, creator, songId);
        SongInPlaylist sip = songInPlaylistService.getSongInPlaylist(sipID).get();
        PlaylistId pID = new PlaylistId(creator, playlistName);
        Playlist playlist = playlistService.findPlaylistById(pID).get();

        sip.getAddedBy().remove(username);

        if (sip.getAddedBy().isEmpty()) {
            playlist.getSongs().remove(sip);
        }
        else {
            songInPlaylistService.save(sip);
        }

        playlistService.savePlaylist(playlist);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
