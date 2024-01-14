package com.example.drumreback.Repositories;

import com.example.drumreback.Entities.SongInPlaylist;
import com.example.drumreback.Entities.SongInPlaylistId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongInPlaylistRepository extends MongoRepository<SongInPlaylist, SongInPlaylistId> {
}
