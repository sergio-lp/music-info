package com.sergio.musicinfo.fragments;


import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sergio.musicinfo.R;
import com.sergio.musicinfo.adapters.ArtistAdapter;
import com.sergio.musicinfo.api.SpotifyService;
import com.sergio.musicinfo.models.Album;
import com.sergio.musicinfo.models.AlbumComplete;
import com.sergio.musicinfo.models.Artist;
import com.sergio.musicinfo.models.SearchResult;
import com.sergio.musicinfo.models.SimpleArtist;
import com.sergio.musicinfo.models.search.ArtistSearch;
import com.sergio.musicinfo.models.search.MultipleArtists;
import com.sergio.musicinfo.models.search.MusicSearch;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumInfoFragment extends Fragment {
    private SpotifyService mService;

    public AlbumInfoFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_album_info, container, false);
        final LinearLayout contentLayout = (LinearLayout) rootView.findViewById(R.id.ll_content_album);
        contentLayout.setVisibility(View.INVISIBLE);

        final Album album = getArguments().getParcelable("paramAlbum");

        final ImageView imgAlbum = (ImageView) rootView.findViewById(R.id.img_album);
        final TextView tvName = (TextView) rootView.findViewById(R.id.tv_name_album);
        final TextView tvRelease = (TextView) rootView.findViewById(R.id.tv_release_date_album);
        final RecyclerView rvArtists = (RecyclerView) rootView.findViewById(R.id.rv_artists_album);

        Button spotifyButton = (Button) rootView.findViewById(R.id.btn_spotify);
        spotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(album.getUri()));
                startActivity(i);
            }
        });

        final ProgressBar progressBar = addProgressBar(rootView);

        mService = new SpotifyService.SpotifyBuilder().build();
        Call<AlbumComplete> call = mService.getAlbumInformation(album.getId());

        call.enqueue(new Callback<AlbumComplete>() {
            @Override
            public void onResponse(Call<AlbumComplete> call, Response<AlbumComplete> response) {
                if (response.isSuccessful()) {
                    tvRelease.setText(Html.fromHtml("<b>" + getString(R.string.release_date) + " </b>" + response.body().getReleaseDate()));
                    tvName.setText(album.getName());

                    StringBuilder builder = new StringBuilder();
                    for (SimpleArtist simpleArtist : album.getArtistList()) {
                        if (album.getArtistList().lastIndexOf(simpleArtist) == album.getArtistList().size() - 1) {
                            builder.append(simpleArtist.getId());
                        } else {
                            builder.append(simpleArtist.getId()).append(",");
                        }
                    }

                    getArtistsInformation(builder.toString(), rvArtists, rootView, progressBar, contentLayout, album, imgAlbum);

                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AlbumComplete> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private void getArtistsInformation(String ids, final RecyclerView rv, final ViewGroup rootView,
                                       final ProgressBar progressBar, final ViewGroup contentLayout, final Album album,
                                       final ImageView imgAlbum) {
        Call<MultipleArtists> call = mService.getArtistsInfo(ids);

        call.enqueue(new Callback<MultipleArtists>() {
            @Override
            public void onResponse(Call<MultipleArtists> call, Response<MultipleArtists> response) {
                if (response.isSuccessful()) {
                    ArtistAdapter adapter = new ArtistAdapter(response.body().getArtistList());
                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    if (response.body().getArtistList().size() > 1) {
                        DividerItemDecoration decoration = new DividerItemDecoration(getContext(),
                                manager.getOrientation());
                        rv.addItemDecoration(decoration);
                    }
                    rv.setLayoutManager(manager);
                    rv.setFocusable(false);
                    rv.setAdapter(adapter);
                    rv.setNestedScrollingEnabled(false);
                    rv.setVisibility(View.VISIBLE);

                    rootView.removeView(progressBar);
                    contentLayout.setVisibility(View.VISIBLE);

                    if (album.getImages().size() >= 1) {
                        Picasso.with(getActivity())
                                .load(album.getImages().get(0).getUrl())
                                .into(imgAlbum);
                    }
                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MultipleArtists> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private ProgressBar addProgressBar(ViewGroup root) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setIndeterminate(true);
        progressBar.setLayoutParams(params);
        root.addView(progressBar);
        return progressBar;
    }

}
