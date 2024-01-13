package com.example.drumreback.Controllers;

import com.example.drumreback.Entities.*;
import com.example.drumreback.RequestBodies.AddToPlaylistRequest;
import com.example.drumreback.RequestBodies.CreatePlaylistRequest;
import com.example.drumreback.RequestBodies.DeleteFromPlaylistRequest;
import com.example.drumreback.RequestBodies.GetLikedSongsRequest;
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
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Boolean> join(@RequestBody GetLikedSongsRequest request) {
        Optional<Playlist> optionalPlaylist = playlistService.findPlaylistById(request.getPlaylistId());

        if (optionalPlaylist.isPresent()) {
            userService.addUserToPlaylist(userService.findUserById(request.getUsername()).get(), playlistService.findPlaylistById(request.getPlaylistId()).get());
            User user = userService.findUserById(request.getUsername()).get();
            user.getInPlaylists().add(request.getPlaylistId());
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
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Boolean> createPlaylist(@RequestBody CreatePlaylistRequest request) {
        PlaylistId playlistId = new PlaylistId(request.getUsername(), request.getPlaylistName());
        if (playlistService.findPlaylistById(playlistId).isPresent())
            return new ResponseEntity<>(false, HttpStatus.OK);

        Playlist playlist = playlistService.addPlaylist(playlistId);

        User user = userService.findUserById(request.getUsername()).get();
        user.getInPlaylists().add(playlistId);
        user.getCreatedPlaylistsNames().add(request.getPlaylistName());
        userService.save(user);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }



    @PostMapping(path = "/addToPlaylist",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Boolean> addToPlaylist(@RequestBody AddToPlaylistRequest request) {
        PlaylistId playlistId = request.getPlaylistId();
        Playlist playlist = playlistService.findPlaylistById(playlistId).get();

        SongInPlaylistId id = new SongInPlaylistId(request.getPlaylistId().getPlaylistName(), request.getPlaylistId().getCreatorUsername(), request.getSong().getSongId());
        if (songInPlaylistService.getSongInPlaylist(id).isPresent()) {
            SongInPlaylist SIP = songInPlaylistService.getSongInPlaylist(id).get();
            SIP.getAddedBy().add(request.getUsername());
            songInPlaylistService.save(SIP);
            playlistService.savePlaylist(playlist);

            System.out.println(SIP.getAddedBy().size());
        }
        else {
            List<String> addedBy = new ArrayList<>();
            addedBy.add(request.getUsername());
            SongInPlaylist songInPlaylist = new SongInPlaylist(id, request.getSong(), addedBy);
            songInPlaylistService.save(songInPlaylist);

            playlist.getSongs().add(songInPlaylist);
            playlistService.savePlaylist(playlist);
        }

        return new ResponseEntity<>(true, HttpStatus.OK);
    }



    @DeleteMapping(path = "/deleteFromPlaylist")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Boolean> deleteFromPlaylist(@RequestBody DeleteFromPlaylistRequest request) {
        SongInPlaylistId sipID = new SongInPlaylistId(request.getPlaylistId().getPlaylistName(), request.getPlaylistId().getCreatorUsername(), request.getSongId());
        SongInPlaylist sip = songInPlaylistService.getSongInPlaylist(sipID).get();
        PlaylistId pID = request.getPlaylistId();
        Playlist playlist = playlistService.findPlaylistById(pID).get();

        sip.getAddedBy().remove(request.getUsername());

        if (sip.getAddedBy().isEmpty()) {
            playlist.getSongs().remove(sip);
            songInPlaylistService.delete(sip);
        }

        songInPlaylistService.save(sip);
        playlistService.savePlaylist(playlist);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }




    @DeleteMapping(path = "/deletePlaylist")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Boolean> deletePlaylist(@RequestBody PlaylistId request) {
        playlistService.delete(request);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}