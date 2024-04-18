package com.example.musiclibraryapplication.service.impl;

import com.example.musiclibraryapplication.model.Artist;
import com.example.musiclibraryapplication.repository.ArtistRepository;
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
import static org.mockito.Mockito.*;

class ArtistServiceImplTest {
    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private ArtistServiceImpl artistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addArtist_Success() {
        Artist artist = new Artist();
        when(artistRepository.save(artist)).thenReturn(artist);

        Artist addedArtist = artistService.addArtist(artist);

        assertNotNull(addedArtist);
        verify(artistRepository, times(1)).save(artist);
    }

    @Test
    void getArtistsBornBeforeAndNationality1999Macedonian_Success() {
        LocalDate dateOfBirthLimit = LocalDate.of(1999, 1, 1);
        String nationality = "Macedonian";

        List<Artist> expectedArtists = Arrays.asList(
                new Artist(1L, "Artist1", "ArtisticName1", LocalDate.of(1990, 1, 1), "Macedonian",null),
                new Artist(2L, "Artist2", "ArtisticName2", LocalDate.of(1995, 1, 1), "Macedonian",null)
        );

        when(artistRepository.findByDateOfBirthBeforeAndNationality(dateOfBirthLimit, nationality))
                .thenReturn(expectedArtists);

        List<Artist> artists = artistService.getArtistsBornBeforeAndNationality1999Macedonian();

        assertEquals(expectedArtists.size(), artists.size());
        verify(artistRepository, times(1)).findByDateOfBirthBeforeAndNationality(dateOfBirthLimit, nationality);
    }

    @Test
    void getArtistWithSongsById_Success() {
        Long artistId = 1L;
        Artist artist = new Artist(artistId, "Artist1", "Artistic1", LocalDate.of(1990, 1, 1), "Macedonian", null);

        when(artistRepository.findArtistWithSongsById(artistId)).thenReturn(Optional.of(artist));

        Optional<Artist> result = artistService.getArtistWithSongsById(artistId);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(artist, result.get());
    }

}