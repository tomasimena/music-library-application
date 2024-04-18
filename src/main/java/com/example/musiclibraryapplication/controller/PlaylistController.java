package com.example.musiclibraryapplication.controller;

import com.example.musiclibraryapplication.model.Playlist;
import com.example.musiclibraryapplication.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;

    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping
    public ResponseEntity<Playlist> addPlaylist(@RequestBody Playlist playlist) {
        Playlist addedPlaylist = playlistService.addPlaylist(playlist);
        return new ResponseEntity<>(addedPlaylist, HttpStatus.CREATED);
    }

    @GetMapping("/byArtist/{artistId}")
    public ResponseEntity<List<Playlist>> getPlaylistsContainingSongsByArtistSortedByArtistInfo(
            @PathVariable Long artistId
    ) {
        List<Playlist> playlists = playlistService.getPlaylistsContainingSongsByArtistSortedByArtistInfo(artistId);

        return new ResponseEntity<>(playlists, HttpStatus.OK);
    }

    @GetMapping("/by-artist/{artistId}")
    public ResponseEntity<List<Playlist>> getPlaylistsByArtistIdOrderedByArtistInfo(@PathVariable Long artistId) {
        List<Playlist> playlists = playlistService.getAllPlaylistsByArtistIdOrderedByArtistInfo(artistId);
        return new ResponseEntity<>(playlists, HttpStatus.OK);
    }

    @GetMapping("/public/maxThreeSongs")
    public ResponseEntity<List<Playlist>> getPublicPlaylistsWithMaxThreeSongs() {
        List<Playlist> playlists = playlistService.getPublicPlaylistsWithMaxThreeSongs();

        return new ResponseEntity<>(playlists, HttpStatus.OK);
    }

    @GetMapping("/{playlistId}/totalDuration")
    public ResponseEntity<Integer> calculateTotalDurationByPlaylistId(@PathVariable Long playlistId) {
        Integer totalDuration = playlistService.calculateTotalDurationByPlaylistId(playlistId);

        if (totalDuration != null) {
            return new ResponseEntity<>(totalDuration, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{playlistId}/addSong/{songId}")
    public ResponseEntity<String> addExistingSongToPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId
    ) {
        boolean added = playlistService.addExistingSongToPlaylist(playlistId, songId);

        if (added) {
            return new ResponseEntity<>("Song added to the playlist successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to add the song to the playlist", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<String> deletePlaylist(@PathVariable Long playlistId) {
        boolean deleted = playlistService.deletePlaylist(playlistId);

        if (deleted) {
            return new ResponseEntity<>("Playlist deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to delete the playlist", HttpStatus.BAD_REQUEST);
        }
    }
}
