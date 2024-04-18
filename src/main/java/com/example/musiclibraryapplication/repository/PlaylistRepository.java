package com.example.musiclibraryapplication.repository;

import com.example.musiclibraryapplication.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Query("SELECT DISTINCT p FROM Playlist p JOIN p.songs s WHERE s.artist.id = :artistId " +
            "ORDER BY s.artist.name, s.artist.dateOfBirth ASC")
    List<Playlist> findPlaylistsContainingSongsByArtistSortedByArtistInfo(
            @Param("artistId") Long artistId
    );
    @Query("SELECT p FROM Playlist p WHERE p.isPrivate = false AND SIZE(p.songs) <= 3")
    List<Playlist> findPublicPlaylistsWithMaxThreeSongs();

    @Query("SELECT DISTINCT p FROM Playlist p " +
            "JOIN FETCH p.songs s " +
            "JOIN FETCH s.artist a " +
            "WHERE a.id = :artistId " +
            "ORDER BY a.name, a.dateOfBirth ASC")
    List<Playlist> findAllByArtistIdOrderedByArtistName(@Param("artistId") Long artistId);
}
