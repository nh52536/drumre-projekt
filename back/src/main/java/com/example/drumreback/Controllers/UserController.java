package com.example.drumreback.Controllers;

import com.example.drumreback.Entities.*;
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
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<List<User>>(userService.getAllUsers(), HttpStatus.OK);
    }



    @PostMapping(path = "/createUser",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //TODO : change to string
    public ResponseEntity<Boolean> create(@RequestBody User user) {

        if (userService.findUserById(user.getUsername()).isPresent())
            return new ResponseEntity<>(false, HttpStatus.OK);

        userService.addUser(user);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }



    @GetMapping("likedSongs")
    //TODO : sve u jedan body
    public ResponseEntity<List<Song>> getLikedSongs(@RequestParam String username, @RequestParam String playlistName, @RequestParam String creator) {
        PlaylistId playlistId = new PlaylistId(creator, playlistName);
        Playlist playlist = playlistService.findPlaylistById(playlistId).get();

        List<Song> ret = new ArrayList<>();

        List<SongInPlaylist> songsInPlaylist = playlist.getSongs();
        for (SongInPlaylist s : songsInPlaylist) {
            if (s.getAddedBy().contains(username))
                ret.add(s.getSong());
        }
// TODO : return List<SongID>=  List<String>
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
}
