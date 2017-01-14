package com.sergio.musicinfo.api;

import com.sergio.musicinfo.models.Album;
import com.sergio.musicinfo.models.AlbumComplete;
import com.sergio.musicinfo.models.Artist;
import com.sergio.musicinfo.models.Music;
import com.sergio.musicinfo.models.SearchResult;
import com.sergio.musicinfo.models.search.AlbumSearch;
import com.sergio.musicinfo.models.search.ArtistSearch;
import com.sergio.musicinfo.models.search.MultipleArtists;
import com.sergio.musicinfo.models.search.MusicSearch;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpotifyService {

    @GET(SpotifyValues.SEARCH_ENDPOINT + "?type=artist")
    Call<ArtistSearch> searchArtists(@Query("q") String query);

    @GET(SpotifyValues.SEARCH_ENDPOINT + "?type=album")
    Call<AlbumSearch> searchAlbums(@Query("q") String query);

    @GET(SpotifyValues.SEARCH_ENDPOINT + "?type=track")
    Call<MusicSearch> searchMusics(@Query("q") String query);

    @GET(SpotifyValues.ARTISTS_ENDPOINT + "{id}/" + SpotifyValues.ALBUMS_ENDPOINT + "?album_type=single,album")
    Call<SearchResult<Album>> getArtistAlbums(@Path("id") String id);

    @GET(SpotifyValues.ARTISTS_ENDPOINT)
    Call<MultipleArtists> getArtistsInfo(@Query("ids") String ids);

    @GET(SpotifyValues.ALBUMS_ENDPOINT + "{id}")
    Call<AlbumComplete> getAlbumInformation(@Path("id") String id);

    @GET(SpotifyValues.ALBUMS_ENDPOINT + "{id}/" + SpotifyValues.TRACKS_ENDPOINT)
    Call<SearchResult<Music>> getAlbumTracks(@Path("id") String id);

    class SpotifyBuilder {

        public SpotifyService build() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SpotifyValues.BASE_URL + SpotifyValues.VERSION)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            return retrofit.create(SpotifyService.class);
        }

    }

}