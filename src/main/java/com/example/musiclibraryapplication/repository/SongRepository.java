package com.example.musiclibraryapplication.repository;

import com.example.musiclibraryapplication.model.Song;
import com.example.musiclibraryapplication.model.enumerations.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByArtistIdAndGenre(Long artistId, Genre genre);
    @Query("SELECT s FROM Song s WHERE s.durationMinutes BETWEEN 5 AND 10 ORDER BY s.durationMinutes DESC")
    List<Song> findFirst3SongsWithDurationBetween5And10Minutes();

}
