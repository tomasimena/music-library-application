package com.example.musiclibraryapplication.service.impl;

import com.example.musiclibraryapplication.model.Playlist;
import com.example.musiclibraryapplication.model.Song;
import com.example.musiclibraryapplication.repository.PlaylistRepository;
import com.example.musiclibraryapplication.repository.SongRepository;
import com.example.musiclibraryapplication.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;

    @Autowired
    public PlaylistServiceImpl(PlaylistRepository playlistRepository, SongRepository songRepository) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
    }

    @Override
    public Playlist addPlaylist(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    @Override
    public List<Playlist> getPlaylistsContainingSongsByArtistSortedByArtistInfo(Long artistId) {
        return playlistRepository.findPlaylistsContainingSongsByArtistSortedByArtistInfo(artistId);
    }

    @Override
    public List<Playlist> getPublicPlaylistsWithMaxThreeSongs() {
        return playlistRepository.findPublicPlaylistsWithMaxThreeSongs();
    }

    @Override
    public Integer calculateTotalDurationByPlaylistId(Long playlistId) {
        Optional<Playlist> playlistOptional = playlistRepository.findById(playlistId);

        if (playlistOptional.isPresent()) {
            Playlist playlist = playlistOptional.get();
            return playlist.getSongs().stream()
                    .mapToInt(Song::getDurationMinutes)
                    .sum();
        } else {
            return null; // Playlist not found
        }
    }

    @Override
    public boolean addExistingSongToPlaylist(Long playlistId, Long songId) {
        Optional<Playlist> playlistOptional = playlistRepository.findById(playlistId);
        Optional<Song> songOptional = songRepository.findById(songId);

        if (playlistOptional.isPresent() && songOptional.isPresent()) {
            Playlist playlist = playlistOptional.get();
            Song song = songOptional.get();

            if (!playlist.getSongs().contains(song)) {
                playlist.getSongs().add(song);
                playlistRepository.save(playlist);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deletePlaylist(Long playlistId) {
        Optional<Playlist> playlistOptional = playlistRepository.findById(playlistId);

        if (playlistOptional.isPresent()) {
            playlistRepository.delete(playlistOptional.get());
            return true;
        }

        return false;
    }

    @Override
    public List<Playlist> getAllPlaylistsByArtistIdOrderedByArtistInfo(Long artistId) {
        return playlistRepository.findAllByArtistIdOrderedByArtistName(artistId);
    }
}
