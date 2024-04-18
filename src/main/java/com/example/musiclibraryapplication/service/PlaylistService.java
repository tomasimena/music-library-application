package com.example.musiclibraryapplication.service;

import com.example.musiclibraryapplication.model.Playlist;

import java.util.List;

public interface PlaylistService {
    Playlist addPlaylist(Playlist playlist);
    List<Playlist> getPlaylistsContainingSongsByArtistSortedByArtistInfo(Long artistId);
    List<Playlist> getPublicPlaylistsWithMaxThreeSongs();
    Integer calculateTotalDurationByPlaylistId(Long playlistId);
    boolean addExistingSongToPlaylist(Long playlistId, Long songId);
    boolean deletePlaylist(Long playlistId);
    List<Playlist> getAllPlaylistsByArtistIdOrderedByArtistInfo(Long artistId);


}
