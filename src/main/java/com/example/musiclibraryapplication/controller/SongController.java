package com.example.musiclibraryapplication.controller;

import com.example.musiclibraryapplication.model.Song;
import com.example.musiclibraryapplication.model.enumerations.Genre;
import com.example.musiclibraryapplication.service.SongService;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/songs")
public class SongController {
    private final SongService songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @PostMapping("/{artistId}")
    public ResponseEntity<Song> addSong(@PathVariable Long artistId, @RequestBody Song song) {
        try {
            Song addedSong = songService.addSong(artistId, song);
            return new ResponseEntity<>(addedSong, HttpStatus.CREATED);
        } catch (EntityExistsException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/longest/{artistId}/{genre}")
    public ResponseEntity<Song> getSongWithLongestDurationByArtistAndGenre(
            @PathVariable Long artistId,
            @PathVariable Genre genre
    ) {
        Optional<Song> songOptional = songService.getSongWithLongestDurationByArtistAndGenre(artistId, genre);

        return songOptional
                .map(song -> new ResponseEntity<>(song, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/first3Between5And10Minutes")
    public ResponseEntity<List<Song>> getFirst3SongsWithDurationBetween5And10Minutes() {
        List<Song> songs = songService.getFirst3SongsWithDurationBetween5And10Minutes();

        return new ResponseEntity<>(songs, HttpStatus.OK);
    }
}
