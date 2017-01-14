package com.sergio.musicinfo.fragments;


import android.content.Intent;
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
import com.sergio.musicinfo.models.Artist;
import com.sergio.musicinfo.models.Music;
import com.sergio.musicinfo.models.SimpleArtist;
import com.sergio.musicinfo.models.search.MultipleArtists;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicInfoFragment extends Fragment {


    public MusicInfoFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_music_info, container, false);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setIndeterminate(true);
        progressBar.setLayoutParams(params);
        rootView.addView(progressBar);

        final Music music = getArguments().getParcelable("paramMusic");

        if (music != null) {

            ImageView imgAlbum = (ImageView) rootView.findViewById(R.id.img_album_music);
            TextView tvMusicName = (TextView) rootView.findViewById(R.id.tv_name_music);
            TextView tvAlbumName = (TextView) rootView.findViewById(R.id.tv_name_album_music);
            TextView tvArtistName = (TextView) rootView.findViewById(R.id.tv_name_artist_music);
            TextView tvPopularity = (TextView) rootView.findViewById(R.id.tv_popularity_music);
            TextView tvDuration = (TextView) rootView.findViewById(R.id.tv_duration_music);
            LinearLayout contentLayout = (LinearLayout) rootView.findViewById(R.id.ll_content_music);
            contentLayout.setVisibility(View.INVISIBLE);

            Button btnSpotify = (Button) rootView.findViewById(R.id.btn_spotify);
            btnSpotify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(music.getUri()));
                    startActivity(i);
                }
            });

            tvMusicName.setText(music.getName());

            if (music.getAlbum() != null) {
                tvAlbumName.setText(music.getAlbum().getName());
            } else {
                tvAlbumName.setText(getString(R.string.no_album));
            }

            if (music.getArtistString() != null) {
                tvArtistName.setText(getString(R.string.song_by) + " " + music.getArtistString());
            } else {
                tvArtistName.setText(getString(R.string.no_artist));
            }

            tvPopularity.setText(Html.fromHtml("<b>" + getString(R.string.popularity) + ": </b>" + music.getPopularity()));
            tvDuration.setText(Html.fromHtml("<b>" + getString(R.string.duration) + ": </b>" + music.getDurationMS()));

            StringBuilder builder = new StringBuilder();
            for (SimpleArtist simpleArtist : music.getArtistList()) {
                if (music.getArtistList().lastIndexOf(simpleArtist) == music.getArtistList().size() - 1) {
                    builder.append(simpleArtist.getId());
                } else {
                    builder.append(simpleArtist.getId()).append(",");
                }
            }

            getArtistsInformation(rootView, progressBar, builder.toString(), contentLayout);

            if (music.getAlbum() != null && music.getAlbum().getImages().size() >= 1) {
                Picasso.with(getActivity())
                        .load(music.getAlbum().getImages().get(0).getUrl())
                        .into(imgAlbum);
            }
        }

        return rootView;
    }

    private void getArtistsInformation(final ViewGroup rootView, final ProgressBar progressBar,
                                       String ids, final View contentView) {
        if (ids != null) {
            System.out.println(ids);
            final RecyclerView rvArtists = (RecyclerView) rootView.findViewById(R.id.rv_artists_music);

            SpotifyService service = new SpotifyService.SpotifyBuilder().build();
            Call<MultipleArtists> call = service.getArtistsInfo(ids);
            call.enqueue(new Callback<MultipleArtists>() {
                @Override
                public void onResponse(Call<MultipleArtists> call, Response<MultipleArtists> response) {
                    rootView.removeView(progressBar);
                    contentView.setVisibility(View.VISIBLE);

                    if (response.isSuccessful()) {
                        List<Artist> artistList = response.body().getArtistList();
                        System.out.println(artistList.size());
                        LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        ArtistAdapter adapter = new ArtistAdapter(artistList);
                        rvArtists.setLayoutManager(manager);
                        rvArtists.setAdapter(adapter);
                        rvArtists.setHasFixedSize(true);
                        rvArtists.setNestedScrollingEnabled(false);
                        if (artistList.size() > 1) {
                            DividerItemDecoration decoration = new DividerItemDecoration(getContext(),
                                    manager.getOrientation());
                            rvArtists.addItemDecoration(decoration);
                        }
                        rvArtists.setVisibility(View.VISIBLE);

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
    }

}
