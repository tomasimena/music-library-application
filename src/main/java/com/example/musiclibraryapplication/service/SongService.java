package com.example.musiclibraryapplication.service;

import com.example.musiclibraryapplication.model.Song;
import com.example.musiclibraryapplication.model.enumerations.Genre;
import jakarta.persistence.EntityExistsException;

import java.util.List;
import java.util.Optional;

public interface SongService {
    Song addSong(Long artistId, Song song) throws EntityExistsException;
    Optional<Song> getSongWithLongestDurationByArtistAndGenre(Long artistId, Genre genre);
    List<Song> getFirst3SongsWithDurationBetween5And10Minutes();
}
