package com.example.musiclibraryapplication.service.impl;

import com.example.musiclibraryapplication.model.Artist;
import com.example.musiclibraryapplication.repository.ArtistRepository;
import com.example.musiclibraryapplication.service.ArtistService;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public Artist addArtist(Artist artist) {
        try {
            if (artistRepository.existsById(artist.getId())) {
                throw new EntityExistsException("Artist with ID " + artist.getId() + " already exists.");
            }
            return artistRepository.save(artist);
        } catch (EntityExistsException e) {
            throw e;
        }

    }
    @Override
    public List<Artist> getArtistsBornBeforeAndNationality1999Macedonian() {
        LocalDate dateOfBirthLimit = LocalDate.of(1999, 1, 1);
        return artistRepository.findByDateOfBirthBeforeAndNationality(dateOfBirthLimit, "Macedonian");
    }

    @Override
    public Optional<Artist> getArtistWithSongsById(Long artistId) {
        return artistRepository.findArtistWithSongsById(artistId);
    }
}
