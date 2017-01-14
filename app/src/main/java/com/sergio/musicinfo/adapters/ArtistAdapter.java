package com.sergio.musicinfo.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sergio.musicinfo.R;
import com.sergio.musicinfo.activities.InformationActivity;
import com.sergio.musicinfo.models.Artist;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistVH> {
    private List<Artist> artistList;

    public ArtistAdapter(List<Artist> artistList) {
        this.artistList = artistList;
    }

    @Override
    public ArtistVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_artist, parent, false);
        return new ArtistVH(view);
    }

    @Override
    public void onBindViewHolder(final ArtistVH holder, int position) {
        Artist artist = artistList.get(position);
        String name = artist.getName();
        String followers = NumberFormat.getNumberInstance(Locale.US).format(artist.getFollowers().getTotal()) + " " +
                holder.tvPopularity.getContext().getString(R.string.followers);
        String popularity = holder.tvPopularity.getContext().getString(R.string.popularity) + ": " + artist.getPopularity();

        if (artist.getImages().size() >= 1) {
            String imageUrl = artist.getImages().get(0).getUrl();
            Picasso.with(holder.imgArtist.getContext())
                    .load(imageUrl)
                    .into(holder.imgArtist);
        }

        holder.tvName.setText(name);
        holder.tvFollowers.setText(followers);
        holder.tvPopularity.setText(popularity);

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.rootView.getContext(), InformationActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable("param", artistList.get(holder.getAdapterPosition()));
                i.putExtras(extras);
                holder.rootView.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    class ArtistVH extends RecyclerView.ViewHolder {
        private CircleImageView imgArtist;
        private TextView tvName, tvFollowers, tvPopularity;
        private View rootView;

        ArtistVH(View itemView) {
            super(itemView);
            imgArtist = (CircleImageView) itemView.findViewById(R.id.img_artist_layout);
            tvName = (TextView) itemView.findViewById(R.id.tv_name_artist_layout);
            tvFollowers = (TextView) itemView.findViewById(R.id.tv_followers_artist_layout);
            tvPopularity = (TextView) itemView.findViewById(R.id.tv_popularity_artist_layout);

            rootView = itemView.findViewById(R.id.artist_layout);
        }
    }
}
