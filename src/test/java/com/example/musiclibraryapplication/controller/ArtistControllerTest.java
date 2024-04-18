package com.example.musiclibraryapplication.controller;

import com.example.musiclibraryapplication.model.Artist;
import com.example.musiclibraryapplication.model.Song;
import com.example.musiclibraryapplication.service.ArtistService;
import com.example.musiclibraryapplication.service.impl.ArtistServiceImpl;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(ArtistController.class)
class ArtistControllerTest {
    @MockBean
    private ArtistService artistService;

    @InjectMocks
    private ArtistController artistController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addArtist_Success() throws Exception {
        Artist artist = new Artist();
        artist.setId(1L);

        Mockito.when(artistService.addArtist(any(Artist.class))).thenReturn(artist);

        mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));

        Mockito.verify(artistService, Mockito.times(1)).addArtist(any(Artist.class));
    }

    @Test
    void addArtist_Conflict() throws Exception {
        Mockito.when(artistService.addArtist(any(Artist.class)))
                .thenThrow(new EntityExistsException("Artist already exists"));

        mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1}"))
                .andExpect(MockMvcResultMatchers.status().isConflict());

        Mockito.verify(artistService, Mockito.times(1)).addArtist(any(Artist.class));
    }

    @Test
    void getArtistsBefore1999Macedonian_Success() throws Exception {
        List<Artist> artists = new ArrayList<>();
        artists.add(new Artist());

        Mockito.when(artistService.getArtistsBornBeforeAndNationality1999Macedonian()).thenReturn(artists);

        mockMvc.perform(MockMvcRequestBuilders.get("/artists/before1999Macedonian"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists());

        Mockito.verify(artistService, Mockito.times(1)).getArtistsBornBeforeAndNationality1999Macedonian();
    }

    @Test
    void getArtistWithSongsById_Exists_Success() throws Exception {
        Artist artist = new Artist();
        artist.setId(1L);
        artist.setSongs(new ArrayList<>());

        Mockito.when(artistService.getArtistWithSongsById(anyLong())).thenReturn(Optional.of(artist));

        mockMvc.perform(MockMvcRequestBuilders.get("/artists/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.songs").isArray());

        Mockito.verify(artistService, Mockito.times(1)).getArtistWithSongsById(anyLong());
    }

    @Test
    void getArtistWithSongsById_NotFound() throws Exception {
        Mockito.when(artistService.getArtistWithSongsById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/artists/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(artistService, Mockito.times(1)).getArtistWithSongsById(anyLong());
    }

    // Add more tests as needed...
}