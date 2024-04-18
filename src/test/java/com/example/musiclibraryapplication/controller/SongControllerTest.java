package com.example.musiclibraryapplication.controller;

import com.example.musiclibraryapplication.model.Song;
import com.example.musiclibraryapplication.model.enumerations.Genre;
import com.example.musiclibraryapplication.service.SongService;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@WebMvcTest(SongController.class)
class SongControllerTest {

    @MockBean
    private SongService songService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addSong_Success() throws Exception {
        // Mocking the service
        Mockito.when(songService.addSong(anyLong(), any(Song.class))).thenReturn(new Song());

        // Perform the request
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/songs/{artistId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void addSong_NotFound() throws Exception {
        // Mocking the service to throw an EntityExistsException
        Mockito.when(songService.addSong(anyLong(), any(Song.class))).thenThrow(EntityExistsException.class);

        // Perform the request
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/songs/{artistId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getSongWithLongestDurationByArtistAndGenre_Exists_Success() throws Exception {
        // Mocking the service
        Mockito.when(songService.getSongWithLongestDurationByArtistAndGenre(anyLong(), any(Genre.class)))
                .thenReturn(Optional.of(new Song()));

        // Perform the request
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/songs/longest/{artistId}/{genre}", 1L, Genre.ROCK)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getSongWithLongestDurationByArtistAndGenre_NotFound() throws Exception {
        // Mocking the service to return an empty optional
        Mockito.when(songService.getSongWithLongestDurationByArtistAndGenre(anyLong(), any(Genre.class)))
                .thenReturn(Optional.empty());

        // Perform the request
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/songs/longest/{artistId}/{genre}", 1L, Genre.ROCK)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getFirst3SongsWithDurationBetween5And10Minutes_Success() throws Exception {
        // Mocking the service
        Mockito.when(songService.getFirst3SongsWithDurationBetween5And10Minutes()).thenReturn(List.of(new Song()));

        // Perform the request
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/songs/first3Between5And10Minutes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}