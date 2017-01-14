package com.sergio.musicinfo.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sergio.musicinfo.R;
import com.sergio.musicinfo.adapters.AlbumAdapter;
import com.sergio.musicinfo.api.SpotifyService;
import com.sergio.musicinfo.models.Album;
import com.sergio.musicinfo.models.SearchResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumsFragment extends Fragment {


    public AlbumsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_albums, container, false);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        final ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setIndeterminate(true);
        progressBar.setLayoutParams(params);
        rootView.addView(progressBar);

        SpotifyService service = new SpotifyService.SpotifyBuilder().build();
        Call<SearchResult<Album>> call = service.getArtistAlbums(getArguments().getString("artistId"));

        call.enqueue(new Callback<SearchResult<Album>>() {
            @Override
            public void onResponse(Call<SearchResult<Album>> call, Response<SearchResult<Album>> response) {
                rootView.removeView(progressBar);
                if (response.isSuccessful()) {

                    if (response.body().getResultList().size() >= 1) {
                        List<Album> albumList = response.body().getResultList();
                        addRecyclerView(rootView, albumList);
                    } else {
                        addTextEmpty(rootView);
                    }

                } else {
                    Log.d("URL", call.request().url().toString());
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchResult<Album>> call, Throwable t) {
                rootView.removeView(progressBar);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private void addTextEmpty(ViewGroup rootView) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(params);
        textView.setText(getString(R.string.no_results));
        rootView.addView(textView);
    }

    private void addRecyclerView(ViewGroup rootView, List<Album> albumList) {
        rootView.removeAllViews();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        AlbumAdapter adapter = new AlbumAdapter(albumList);
        RecyclerView recyclerView = new RecyclerView(getActivity());
        if (albumList.size() > 1) {
            DividerItemDecoration decoration = new DividerItemDecoration(getContext(),
                    manager.getOrientation());
            recyclerView.addItemDecoration(decoration);
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setFocusable(false);
        rootView.addView(recyclerView);
    }

}
