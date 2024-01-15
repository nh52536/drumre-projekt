package com.example.drumreback.Controllers;

import com.example.drumreback.Entities.*;
import com.example.drumreback.RequestBodies.*;
import com.example.drumreback.Services.PlaylistService;
import com.example.drumreback.Services.SongInPlaylistService;
import com.example.drumreback.Services.SongService;
import com.example.drumreback.Services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
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



    @PostMapping(path = "/deleteFromPlaylist",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Boolean> deleteFromPlaylist(@RequestBody DeleteFromPlaylistRequest request) {
        SongInPlaylistId sipID = new SongInPlaylistId(request.getPlaylistId().getPlaylistName(), request.getPlaylistId().getCreatorUsername(), request.getSongId());
        SongInPlaylist sip = songInPlaylistService.getSongInPlaylist(sipID).get();
        PlaylistId pID = request.getPlaylistId();
        Playlist playlist = null;
        if (playlistService.findPlaylistById(pID).isPresent())
            playlist = playlistService.findPlaylistById(pID).get();

        sip.getAddedBy().remove(request.getUsername());

        if (sip.getAddedBy().isEmpty()) {
            if (playlist != null) {
                for (SongInPlaylist x : playlist.getSongs())
                    if (x.getSongInPlaylistId().equals(sip.getSongInPlaylistId())) {
                        playlist.getSongs().remove(x);
                        break;
                    }
            }
            songInPlaylistService.delete(sip);
        }
        else
            songInPlaylistService.save(sip);
        if (playlist != null) playlistService.savePlaylist(playlist);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }




    @DeleteMapping(path = "/deletePlaylist")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Boolean> deletePlaylist(@RequestBody PlaylistId request) {
        playlistService.delete(request);

        User user = userService.findUserById(request.getCreatorUsername()).get();
        user.getCreatedPlaylistsNames().remove(request.getPlaylistName());
        user.getInPlaylists().remove(request);
        userService.save(user);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }




    @PostMapping(path = "/createCustomPlaylist",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> createCustomPlaylist(@RequestBody CreateCustomPlaylistRequest request) throws URISyntaxException, JsonProcessingException {
        String playlistUri = playlistService.createCustomPlaylist(request.getToken(), request.getPlaylistId(), request.getId());

        return new ResponseEntity<>(playlistUri, HttpStatus.OK);
    }
}
