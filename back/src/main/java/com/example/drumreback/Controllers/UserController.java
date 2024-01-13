package com.example.drumreback.Controllers;

import com.example.drumreback.Entities.*;
import com.example.drumreback.RequestBodies.GetLikedSongsRequest;
import com.example.drumreback.Services.PlaylistService;
import com.example.drumreback.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    PlaylistService playlistService;

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping("users")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<List<User>>(userService.getAllUsers(), HttpStatus.OK);
    }



    @PostMapping(path = "/createUser",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Boolean> create(@RequestBody String username) {

        if (userService.findUserById(username).isPresent())
            return new ResponseEntity<>(false, HttpStatus.OK);

        List<String> ls = new ArrayList<>();
        List<PlaylistId> lp = new ArrayList<>();
        User user = new User(username, ls, lp);

        userService.addUser(user);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }



    @GetMapping("likedSongs")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<String>> getLikedSongs(@RequestBody GetLikedSongsRequest request) {
        PlaylistId playlistId = request.getPlaylistId();
        Playlist playlist = playlistService.findPlaylistById(playlistId).get();

        List<String> ret = new ArrayList<>();

        List<SongInPlaylist> songsInPlaylist = playlist.getSongs();
        for (SongInPlaylist s : songsInPlaylist) {
            if (s.getAddedBy().contains(request.getUsername()))
                ret.add(s.getSong().getSongId());
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
}
