package com.sergio.musicinfo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sergio.musicinfo.R;
import com.sergio.musicinfo.adapters.MusicAdapter;
import com.sergio.musicinfo.api.SpotifyService;
import com.sergio.musicinfo.models.Album;
import com.sergio.musicinfo.models.Music;
import com.sergio.musicinfo.models.SearchResult;
import com.sergio.musicinfo.models.search.MusicSearch;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicsFragment extends Fragment {

    public MusicsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_musics, container, false);

        final Album album = getArguments().getParcelable("paramAlbum");

        if (album != null) {

            SpotifyService service = new SpotifyService.SpotifyBuilder().build();
            Call<SearchResult<Music>> call = service.getAlbumTracks(album.getId());

            call.enqueue(new Callback<SearchResult<Music>>() {
                @Override
                public void onResponse(Call<SearchResult<Music>> call, Response<SearchResult<Music>> response) {
                    if (response.isSuccessful()) {
                        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                        List<Music> musicList = response.body().getResultList();

                        for (Music music : musicList) {
                            music.setAlbum(album);
                        }

                        MusicAdapter adapter = new MusicAdapter(musicList);
                        RecyclerView rvMusics = new RecyclerView(getActivity());
                        rvMusics.setHasFixedSize(false);
                        rvMusics.setLayoutManager(manager);
                        rvMusics.setFocusable(false);
                        rvMusics.setAdapter(adapter);
                        rootView.addView(rvMusics);
                    } else {
                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SearchResult<Music>> call, Throwable t) {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        return rootView;
    }
}
