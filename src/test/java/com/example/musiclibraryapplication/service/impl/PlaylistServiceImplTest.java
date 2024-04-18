package com.example.musiclibraryapplication.service.impl;

import com.example.musiclibraryapplication.model.Playlist;
import com.example.musiclibraryapplication.model.Song;
import com.example.musiclibraryapplication.repository.PlaylistRepository;
import com.example.musiclibraryapplication.repository.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlaylistServiceImplTest {

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private PlaylistServiceImpl playlistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addPlaylist_ShouldReturnAddedPlaylist() {
        // Arrange
        Playlist playlist = new Playlist(1L, "My Playlist", LocalDate.now(), false, new ArrayList<>());
        when(playlistRepository.save(any(Playlist.class))).thenReturn(playlist);

        // Act
        Playlist addedPlaylist = playlistService.addPlaylist(playlist);

        // Assert
        assertNotNull(addedPlaylist);
        assertEquals(playlist.getName(), addedPlaylist.getName());
    }

    @Test
    void getPlaylistsContainingSongsByArtistSortedByArtistInfo_ShouldReturnPlaylists() {
        // Arrange
        Long artistId = 1L;
        List<Playlist> playlists = Arrays.asList(
                new Playlist(1L, "Playlist1", LocalDate.now(), false, new ArrayList<>()),
                new Playlist(2L, "Playlist2", LocalDate.now(), true, new ArrayList<>())
        );
        when(playlistRepository.findPlaylistsContainingSongsByArtistSortedByArtistInfo(artistId)).thenReturn(playlists);

        // Act
        List<Playlist> result = playlistService.getPlaylistsContainingSongsByArtistSortedByArtistInfo(artistId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Playlist1", result.get(0).getName());
        assertEquals("Playlist2", result.get(1).getName());
    }

    @Test
    void getPublicPlaylistsWithMaxThreeSongs_ShouldReturnPublicPlaylists() {
        // Arrange
        List<Playlist> playlists = Arrays.asList(
                new Playlist(1L, "Playlist1", LocalDate.now(), false, new ArrayList<>()),
                new Playlist(2L, "Playlist2", LocalDate.now(), true, Arrays.asList(new Song()))
        );
        when(playlistRepository.findPublicPlaylistsWithMaxThreeSongs()).thenReturn(playlists);

        // Act
        List<Playlist> result = playlistService.getPublicPlaylistsWithMaxThreeSongs();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Playlist1", result.get(0).getName());
        assertEquals("Playlist2", result.get(1).getName());
    }

    @Test
    void calculateTotalDurationByPlaylistId_ShouldReturnTotalDuration() {
        // Arrange
        Long playlistId = 1L;
        Playlist playlist = new Playlist(playlistId, "My Playlist", LocalDate.now(), false, Arrays.asList(
                new Song(1L, "Song1", 5, LocalDate.now(), null, null, null),
                new Song(2L, "Song2", 3, LocalDate.now(), null, null, null)
        ));
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));

        // Act
        Integer result = playlistService.calculateTotalDurationByPlaylistId(playlistId);

        // Assert
        assertEquals(8, result);
    }

    @Test
    void calculateTotalDurationByPlaylistId_InvalidPlaylistId_ShouldReturnNull() {
        // Arrange
        Long playlistId = 1L;
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.empty());

        // Act
        Integer result = playlistService.calculateTotalDurationByPlaylistId(playlistId);

        // Assert
        assertNull(result);
    }

    @Test
    void addExistingSongToPlaylist_ShouldReturnTrueOnSuccess() {
        // Arrange
        Long playlistId = 1L;
        Long songId = 2L;
        Playlist playlist = new Playlist(playlistId, "My Playlist", LocalDate.now(), false, new ArrayList<>());
        Song song = new Song(songId, "Song1", 4, LocalDate.now(), null, null, null);
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        when(songRepository.findById(songId)).thenReturn(Optional.of(song));

        // Act
        boolean result = playlistService.addExistingSongToPlaylist(playlistId, songId);

        // Assert
        assertTrue(result);
        assertTrue(playlist.getSongs().contains(song));
        verify(playlistRepository, times(1)).save(playlist);
    }

    @Test
    void addExistingSongToPlaylist_SongAlreadyExists_ShouldReturnFalse() {
        // Arrange
        Long playlistId = 1L;
        Long songId = 2L;

        Playlist playlist = new Playlist();
        playlist.setId(playlistId);
        Song song = new Song();
        song.setId(songId);
        playlist.setSongs(Collections.singletonList(song));

        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        when(songRepository.findById(songId)).thenReturn(Optional.of(song));

        // Act
        boolean result = playlistService.addExistingSongToPlaylist(playlistId, songId);

        // Assert
        assertFalse(result);
        verify(playlistRepository, never()).save(any());
    }

    @Test
    void deletePlaylist_ShouldReturnTrueOnSuccess() {
        // Arrange
        Long playlistId = 1L;
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(new Playlist()));

        // Act
        boolean result = playlistService.deletePlaylist(playlistId);

        // Assert
        assertTrue(result);
        verify(playlistRepository, times(1)).delete(any(Playlist.class));
    }

    @Test
    void deletePlaylist_InvalidPlaylistId_ShouldReturnFalse() {
        // Arrange
        Long playlistId = 1L;
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.empty());

        // Act
        boolean result = playlistService.deletePlaylist(playlistId);

        // Assert
        assertFalse(result);
        verify(playlistRepository, times(0)).delete(any(Playlist.class));
    }

    @Test
    void getAllPlaylistsByArtistIdOrderedByArtistInfo_ShouldReturnPlaylists() {
        // Arrange
        Long artistId = 1L;
        List<Playlist> playlists = Arrays.asList(
                new Playlist(1L, "Playlist1", LocalDate.now(), false, Arrays.asList(new Song())),
                new Playlist(2L, "Playlist2", LocalDate.now(), true, new ArrayList<>())
        );
        when(playlistRepository.findAllByArtistIdOrderedByArtistName(artistId)).thenReturn(playlists);

        // Act
        List<Playlist> result = playlistService.getAllPlaylistsByArtistIdOrderedByArtistInfo(artistId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Playlist1", result.get(0).getName());
        assertEquals("Playlist2", result.get(1).getName());
    }

}