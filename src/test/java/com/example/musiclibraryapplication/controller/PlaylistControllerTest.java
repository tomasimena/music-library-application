package com.example.musiclibraryapplication.controller;

import com.example.musiclibraryapplication.model.Playlist;
import com.example.musiclibraryapplication.service.PlaylistService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(PlaylistController.class)
class PlaylistControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaylistService playlistService;

    @Test
    void testAddPlaylist() throws Exception {
        Playlist playlist = new Playlist();
        Mockito.when(playlistService.addPlaylist(playlist)).thenReturn(playlist);

        mockMvc.perform(MockMvcRequestBuilders.post("/playlists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void testGetPlaylistsContainingSongsByArtistSortedByArtistInfo() throws Exception {
        Long artistId = 1L;
        List<Playlist> playlists = Arrays.asList(new Playlist(), new Playlist());
        Mockito.when(playlistService.getPlaylistsContainingSongsByArtistSortedByArtistInfo(artistId)).thenReturn(playlists);

        mockMvc.perform(MockMvcRequestBuilders.get("/playlists/byArtist/{artistId}", artistId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void testGetPlaylistsByArtistIdOrderedByArtistInfo() throws Exception {
        Long artistId = 1L;
        List<Playlist> playlists = Arrays.asList(new Playlist(), new Playlist());
        Mockito.when(playlistService.getAllPlaylistsByArtistIdOrderedByArtistInfo(artistId)).thenReturn(playlists);

        mockMvc.perform(MockMvcRequestBuilders.get("/playlists/by-artist/{artistId}", artistId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetPublicPlaylistsWithMaxThreeSongs() throws Exception {
        List<Playlist> playlists = Arrays.asList(new Playlist(), new Playlist());
        Mockito.when(playlistService.getPublicPlaylistsWithMaxThreeSongs()).thenReturn(playlists);

        mockMvc.perform(MockMvcRequestBuilders.get("/playlists/public/maxThreeSongs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testCalculateTotalDurationByPlaylistId() throws Exception {
        Long playlistId = 1L;
        Integer totalDuration = 100; // Assuming a total duration value
        Mockito.when(playlistService.calculateTotalDurationByPlaylistId(playlistId)).thenReturn(totalDuration);

        mockMvc.perform(MockMvcRequestBuilders.get("/playlists/{playlistId}/totalDuration", playlistId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testAddExistingSongToPlaylist() throws Exception {
        Long playlistId = 1L;
        Long songId = 2L;
        Mockito.when(playlistService.addExistingSongToPlaylist(playlistId, songId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/playlists/{playlistId}/addSong/{songId}", playlistId, songId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeletePlaylist() throws Exception {
        Long playlistId = 1L;
        Mockito.when(playlistService.deletePlaylist(playlistId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/playlists/{playlistId}", playlistId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}