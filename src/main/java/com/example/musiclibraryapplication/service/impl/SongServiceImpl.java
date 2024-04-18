package com.example.musiclibraryapplication.service.impl;

import com.example.musiclibraryapplication.model.Song;
import com.example.musiclibraryapplication.model.enumerations.Genre;
import com.example.musiclibraryapplication.repository.ArtistRepository;
import com.example.musiclibraryapplication.repository.SongRepository;
import com.example.musiclibraryapplication.service.SongService;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SongServiceImpl implements SongService {
    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;

    @Autowired
    public SongServiceImpl(SongRepository songRepository, ArtistRepository artistRepository) {
        this.songRepository = songRepository;
        this.artistRepository = artistRepository;
    }

    @Override
    public Song addSong(Long artistId, Song song) throws EntityExistsException {
        if (!artistRepository.existsById(artistId)) {
            throw new EntityExistsException("Artist with ID " + artistId + " not found.");
        }
        artistRepository.findById(artistId).ifPresent(song::setArtist);
        return songRepository.save(song);
    }

    @Override
    public Optional<Song> getSongWithLongestDurationByArtistAndGenre(Long artistId, Genre genre) {
        List<Song> songs = songRepository.findByArtistIdAndGenre(artistId, genre);

        // Find the song with the longest duration
        return songs.stream()
                .max(Comparator.comparing(Song::getDurationMinutes));
    }

    @Override
    public List<Song> getFirst3SongsWithDurationBetween5And10Minutes() {
        return songRepository.findFirst3SongsWithDurationBetween5And10Minutes();
    }
}
