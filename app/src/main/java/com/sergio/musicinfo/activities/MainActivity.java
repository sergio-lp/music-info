package com.sergio.musicinfo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.sergio.musicinfo.R;

public class MainActivity extends AppCompatActivity {
    static String ARTIST_ACTION = "artists";
    static String ALBUM_ACTION = "albums";
    static String MUSIC_ACTION = "musics";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FloatingActionMenu fabMenu = (FloatingActionMenu) findViewById(R.id.fab_menu_main);
        FloatingActionButton fabArtists = (FloatingActionButton) findViewById(R.id.fab_artists_main);
        FloatingActionButton fabAlbums = (FloatingActionButton) findViewById(R.id.fab_albums_main);
        FloatingActionButton fabMusics = (FloatingActionButton) findViewById(R.id.fab_musics_main);

        fabArtists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabMenu.close(true);
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                i.setAction(ARTIST_ACTION);
                startActivity(i);
            }
        });

        fabAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabMenu.close(true);
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                i.setAction(ALBUM_ACTION);
                startActivity(i);
            }
        });

        fabMusics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabMenu.close(true);
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                i.setAction(MUSIC_ACTION);
                startActivity(i);
            }
        });

    }
}
