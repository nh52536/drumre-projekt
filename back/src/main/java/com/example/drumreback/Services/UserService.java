package com.example.drumreback.Services;

import com.example.drumreback.Entities.Playlist;
import com.example.drumreback.Entities.PlaylistId;
import com.example.drumreback.Entities.User;
import com.example.drumreback.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    PlaylistService playlistService;

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> findUserById(String username) {
        return userRepository.findById(username);
    }

    public void addUserToPlaylist(User user, Playlist playlist) {

        if (playlist.getUsers().contains(user.getUsername())) return;

        playlist.getUsers().add(user.getUsername());
        user.getInPlaylists().add(playlist.getPlaylistId());

        save(user);
        playlistService.savePlaylist(playlist);
    }

    public void save(User user) {
        userRepository.save(user);
    }

}
