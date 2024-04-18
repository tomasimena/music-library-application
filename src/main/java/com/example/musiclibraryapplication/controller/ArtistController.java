package com.example.musiclibraryapplication.controller;

import com.example.musiclibraryapplication.model.Artist;
import com.example.musiclibraryapplication.model.Song;
import com.example.musiclibraryapplication.service.ArtistService;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/artists")
public class ArtistController {
    private final ArtistService artistService;

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @PostMapping
    public ResponseEntity<Artist> addArtist(@RequestBody Artist artist) {
        try {
            Artist addedArtist = artistService.addArtist(artist);
            return new ResponseEntity<>(addedArtist, HttpStatus.CREATED);
        } catch (EntityExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/before1999Macedonian")
    public ResponseEntity<List<Artist>> getArtistsBefore1999Macedonian() {
        List<Artist> artists = artistService.getArtistsBornBeforeAndNationality1999Macedonian();
        return ResponseEntity.ok(artists);
    }

    @GetMapping("/{artistId}")
    public ResponseEntity<Artist> getArtistWithSongsById(@PathVariable Long artistId) {
        Optional<Artist> artistOptional = artistService.getArtistWithSongsById(artistId);

        if (artistOptional.isPresent()) {
            Artist artist = artistOptional.get();
            // Sort the list of songs by title in descending order
            artist.getSongs().sort(Comparator.comparing(Song::getTitle).reversed());
            return new ResponseEntity<>(artist, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
