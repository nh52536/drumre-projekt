package com.example.drumreback.Repositories;

import com.example.drumreback.Entities.Song;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends MongoRepository<Song, Integer> {
}
