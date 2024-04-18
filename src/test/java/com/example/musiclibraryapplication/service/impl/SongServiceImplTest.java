package com.example.musiclibraryapplication.service.impl;

import com.example.musiclibraryapplication.model.Artist;
import com.example.musiclibraryapplication.model.Song;
import com.example.musiclibraryapplication.model.enumerations.Genre;
import com.example.musiclibraryapplication.repository.ArtistRepository;
import com.example.musiclibraryapplication.repository.SongRepository;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SongServiceImplTest {
    @Mock
    private SongRepository songRepository;

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private SongServiceImpl songService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addSong_ValidArtistIdAndSong_ShouldReturnAddedSong() {
        // Arrange
        Long artistId = 1L;
        Song song = new Song(1L, "Song Title", 4, LocalDate.now(), Genre.POP, null, null);
        when(artistRepository.existsById(artistId)).thenReturn(true);
        when(artistRepository.findById(artistId)).thenReturn(Optional.of(new Artist()));
        when(songRepository.save(any(Song.class))).thenReturn(song);

        // Act
        Song addedSong = songService.addSong(artistId, song);

        // Assert
        assertNotNull(addedSong);
        assertEquals(song.getTitle(), addedSong.getTitle());
    }

    @Test
    void addSong_InvalidArtistId_ShouldThrowEntityExistsException() {
        // Arrange
        Long artistId = 1L;
        Song song = new Song(1L, "Song Title", 4, LocalDate.now(), Genre.POP, null, null);
        when(artistRepository.existsById(artistId)).thenReturn(false);

        // Act and Assert
        assertThrows(EntityExistsException.class, () -> songService.addSong(artistId, song));
    }

    @Test
    void getSongWithLongestDurationByArtistAndGenre_ShouldReturnSongWithLongestDuration() {
        // Arrange
        Long artistId = 1L;
        Genre genre = Genre.ROCK;
        List<Song> songs = Arrays.asList(
                new Song(1L, "Song1", 3, LocalDate.now(), genre, null, null),
                new Song(2L, "Song2", 5, LocalDate.now(), genre, null, null),
                new Song(3L, "Song3", 4, LocalDate.now(), genre, null, null)
        );
        when(songRepository.findByArtistIdAndGenre(artistId, genre)).thenReturn(songs);

        // Act
        Optional<Song> result = songService.getSongWithLongestDurationByArtistAndGenre(artistId, genre);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Song2", result.get().getTitle());
    }

    @Test
    void getFirst3SongsWithDurationBetween5And10Minutes_ShouldReturnListOfSongs() {
        // Arrange
        List<Song> songs = Arrays.asList(
                new Song(1L, "Song1", 7, LocalDate.now(), Genre.POP, null, null),
                new Song(2L, "Song2", 9, LocalDate.now(), Genre.ROCK, null, null),
                new Song(3L, "Song3", 6, LocalDate.now(), Genre.HIP_HOP, null, null)
        );
        when(songRepository.findFirst3SongsWithDurationBetween5And10Minutes()).thenReturn(songs);

        // Act
        List<Song> result = songService.getFirst3SongsWithDurationBetween5And10Minutes();

        // Assert
        assertEquals(3, result.size());
        assertEquals("Song1", result.get(0).getTitle());
        assertEquals("Song2", result.get(1).getTitle());
        assertEquals("Song3", result.get(2).getTitle());
    }
}