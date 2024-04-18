package com.example.musiclibraryapplication.repository;

import com.example.musiclibraryapplication.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    boolean existsById(Long id);
    List<Artist> findByDateOfBirthBeforeAndNationality(LocalDate dateOfBirth, String nationality);

    @Query("SELECT a FROM Artist a LEFT JOIN FETCH a.songs s WHERE a.id = :artistId")
    Optional<Artist> findArtistWithSongsById(@Param("artistId") Long artistId);
}

