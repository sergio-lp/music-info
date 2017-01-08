package com.sergio.musicinfo.activities;

import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.sergio.musicinfo.R;
import com.sergio.musicinfo.fragments.AlbumInfoFragment;
import com.sergio.musicinfo.fragments.AlbumsFragment;
import com.sergio.musicinfo.fragments.ArtistInfoFragment;
import com.sergio.musicinfo.fragments.MusicInfoFragment;
import com.sergio.musicinfo.fragments.MusicsFragment;
import com.sergio.musicinfo.models.Album;
import com.sergio.musicinfo.models.Artist;
import com.sergio.musicinfo.models.Music;

import java.util.ArrayList;
import java.util.List;

public class InformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        ViewPager pager = (ViewPager) findViewById(R.id.info_viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_info);


        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        Parcelable object = extras.getParcelable("param");

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (object instanceof Artist) {
            Artist artist = (Artist) object;
            setTitle(artist.getName());

            Fragment artistInfo = new ArtistInfoFragment();
            Bundle artistBundle = new Bundle();
            artistBundle.putParcelable("paramArtist", artist);
            artistInfo.setArguments(artistBundle);

            Fragment albums = new AlbumsFragment();
            Bundle albumBundle = new Bundle();
            albumBundle.putString("artistId", artist.getId());
            albums.setArguments(albumBundle);

            adapter.addItem(artistInfo, getString(R.string.information));
            adapter.addItem(albums, getString(R.string.albums));

        } else if (object instanceof Album) {
            Album album = (Album) object;
            setTitle(album.getName());

            Fragment albumInfo = new AlbumInfoFragment();
            Bundle albumBundle = new Bundle();
            albumBundle.putParcelable("paramAlbum", album);
            albumInfo.setArguments(albumBundle);

            Fragment musicsFragment = new MusicsFragment();
            musicsFragment.setArguments(albumBundle);

            adapter.addItem(albumInfo, getString(R.string.information));
            adapter.addItem(musicsFragment, getString(R.string.musics));
        } else if (object instanceof Music) {
            Music music = (Music) object;
            setTitle(music.getName());

            Fragment musicInfo = new MusicInfoFragment();
            Bundle musicBundle = new Bundle();
            musicBundle.putParcelable("paramMusic", music);
            musicInfo.setArguments(musicBundle);

            Fragment albumMusics = new MusicsFragment();
            Bundle args = new Bundle();
            args.putParcelable("paramAlbum", music.getAlbum());
            albumMusics.setArguments(args);


            adapter.addItem(musicInfo, getString(R.string.information));
            adapter.addItem(albumMusics, getString(R.string.album_musics));
        }

        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragmentList = new ArrayList<>();
        List<String> titles = new ArrayList<>();


        private ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private void addItem(Fragment fragment, String title) {
            fragmentList.add(fragment);
            titles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}
