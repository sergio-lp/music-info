package com.sergio.musicinfo.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sergio.musicinfo.R;
import com.sergio.musicinfo.activities.InformationActivity;
import com.sergio.musicinfo.activities.SearchActivity;
import com.sergio.musicinfo.models.Album;
import com.sergio.musicinfo.models.Image;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumVH> {
    private List<Album> albumList;

    public AlbumAdapter(List<Album> albumList) {
        this.albumList = albumList;
    }

    @Override
    public AlbumVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_album, parent, false);
        return new AlbumVH(view);
    }

    @Override
    public void onBindViewHolder(final AlbumVH holder, int position) {
        Album album = albumList.get(position);
        String name = album.getName();
        String artistsName = album.getArtistString();
        List<Image> images = album.getImages();

        holder.tvName.setText(name);
        holder.tvArtists.setText(artistsName);

        if (images.size() >= 1) {
            Image image = images.get(0);
            Picasso.with(holder.imgAlbumArt.getContext())
                    .load(image.getUrl())
                    .into(holder.imgAlbumArt);
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.rootView.getContext(), InformationActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable("param", albumList.get(holder.getAdapterPosition()));
                i.putExtras(extras);
                holder.rootView.getContext().startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }

    class AlbumVH extends RecyclerView.ViewHolder {
        private ImageView imgAlbumArt;
        private TextView tvName, tvArtists;
        private View rootView;

        AlbumVH(View itemView) {
            super(itemView);
            imgAlbumArt = (ImageView) itemView.findViewById(R.id.img_art_album_layout);
            tvName = (TextView) itemView.findViewById(R.id.tv_name_album_layout);
            tvArtists = (TextView) itemView.findViewById(R.id.tv_artist_album_layout);
            rootView = itemView.findViewById(R.id.album_layout);
        }
    }
}
