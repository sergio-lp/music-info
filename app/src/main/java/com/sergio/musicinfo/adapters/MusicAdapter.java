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
import com.sergio.musicinfo.models.Music;
import com.sergio.musicinfo.models.SimpleArtist;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicVH> {
    private List<Music> musicList;

    public MusicAdapter(List<Music> musicList) {
        this.musicList = musicList;
    }

    @Override
    public MusicVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_music, parent, false);
        return new MusicVH(view);
    }

    @Override
    public void onBindViewHolder(final MusicVH holder, int position) {
        Music music = musicList.get(position);
        String name = music.getName();
        String albumName = holder.tvMusicName.getContext().getString(R.string.no_album);
        if (music.getAlbum() != null) {
            albumName = music.getAlbum().getName();
        }
        String artistsName = music.getArtistString();

        String musicInfo = albumName + holder.tvMusicName.getContext().getString(R.string.separator) + artistsName;
        holder.tvMusicName.setText(name);
        holder.tvMusicInfo.setText(musicInfo);

        if (!music.isExplicit()) {
            holder.tvExplicit.setVisibility(View.GONE);
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.rootView.getContext(), InformationActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable("param", musicList.get(holder.getAdapterPosition()));
                i.putExtras(extras);
                holder.rootView.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    class MusicVH extends RecyclerView.ViewHolder {
        private View rootView;
        private TextView tvMusicName, tvMusicInfo, tvExplicit;

        MusicVH(View itemView) {
            super(itemView);
            tvMusicName = (TextView) itemView.findViewById(R.id.tv_name_music_layout);
            tvMusicInfo = (TextView) itemView.findViewById(R.id.tv_info_music_layout);
            tvExplicit = (TextView) itemView.findViewById(R.id.tv_explicit_music_layout);
            rootView = itemView.findViewById(R.id.music_layout);
        }
    }
}
