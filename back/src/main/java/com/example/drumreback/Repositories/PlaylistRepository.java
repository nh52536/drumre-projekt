package com.example.drumreback.Repositories;

import com.example.drumreback.Entities.Playlist;
import com.example.drumreback.Entities.PlaylistId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaylistRepository extends MongoRepository<Playlist, PlaylistId> {
}
