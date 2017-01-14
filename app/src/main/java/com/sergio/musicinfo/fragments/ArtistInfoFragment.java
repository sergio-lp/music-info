package com.sergio.musicinfo.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sergio.musicinfo.R;
import com.sergio.musicinfo.models.Artist;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;

public class ArtistInfoFragment extends Fragment {


    public ArtistInfoFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        final Artist artist = args.getParcelable("paramArtist");

        View rootView = inflater.inflate(R.layout.fragment_artist_info, container, false);
        CircleImageView imgArtist = (CircleImageView) rootView.findViewById(R.id.img_artist);
        TextView tvName = (TextView) rootView.findViewById(R.id.tv_name_artist);
        TextView tvFollowers = (TextView) rootView.findViewById(R.id.tv_followers_artist);
        TextView tvPopularity = (TextView) rootView.findViewById(R.id.tv_popularity_artist);
        TextView tvGenres = (TextView) rootView.findViewById(R.id.tv_genres_artist);

        Button spotifyButton = (Button) rootView.findViewById(R.id.btn_spotify);
        spotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(artist.getUri()));
                startActivity(i);
            }
        });

        String followers = getString(R.string.followers);
        followers = followers.substring(0, 1).toUpperCase() + followers.substring(1);

        tvName.setText(artist.getName());
        followers = "<b>" + followers + ": </b>" + NumberFormat.getNumberInstance().format(artist.getFollowers().getTotal());
        String popularity = "<b>" + getString(R.string.popularity) + ": </b>" + artist.getPopularity();

        String genres = "<b>" + getString(R.string.genres) + ": </b>";
        if (artist.getGenres() != null) {
            genres = genres + artist.getGenres();
        } else {
            genres = genres + getString(R.string.not_found);
        }

        tvFollowers.setText(Html.fromHtml(followers));
        tvPopularity.setText(Html.fromHtml(popularity));
        tvGenres.setText(Html.fromHtml(genres));

        if (artist.getImages().size() >= 1) {
            Picasso.with(getActivity())
                    .load(artist.getImages().get(0).getUrl())
                    .into(imgArtist);

        }

        return rootView;
    }

}
