package com.example.musiclibraryapplication.service;

import com.example.musiclibraryapplication.model.Artist;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ArtistService {
    Artist addArtist(Artist artist);
    List<Artist> getArtistsBornBeforeAndNationality1999Macedonian();
    Optional<Artist> getArtistWithSongsById(Long artistId);


}
