package com.sergio.musicinfo.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sergio.musicinfo.R;
import com.sergio.musicinfo.adapters.AlbumAdapter;
import com.sergio.musicinfo.adapters.ArtistAdapter;
import com.sergio.musicinfo.adapters.MusicAdapter;
import com.sergio.musicinfo.api.SpotifyService;
import com.sergio.musicinfo.models.Album;
import com.sergio.musicinfo.models.Artist;
import com.sergio.musicinfo.models.Music;
import com.sergio.musicinfo.models.search.AlbumSearch;
import com.sergio.musicinfo.models.search.ArtistSearch;
import com.sergio.musicinfo.models.search.MusicSearch;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private ViewGroup mRootView;
    private ProgressBar mProgressBar;
    private String ACTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mRootView = (ViewGroup) findViewById(R.id.activity_search);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setTitle(getString(R.string.search));

        ACTION = getIntent().getAction();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
            addProgressBar();
            String query = intent.getStringExtra(SearchManager.QUERY);
            handleSearchIntent(query);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.item_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        if (ACTION.equals(MainActivity.ARTIST_ACTION)) {
            searchView.setQueryHint(getString(R.string.search_artist));
        } else if (ACTION.equals(MainActivity.ALBUM_ACTION)) {
            searchView.setQueryHint(getString(R.string.search_album));
        } else {
            searchView.setQueryHint(getString(R.string.search_music));
        }

        searchView.onActionViewExpanded();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    private void addProgressBar() {
        mRootView.removeAllViews();
        if (mProgressBar == null) {
            CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            mProgressBar = new ProgressBar(SearchActivity.this);
            mProgressBar.setIndeterminate(true);
            mProgressBar.setLayoutParams(params);
            mRootView.addView(mProgressBar);
        } else {
            mRootView.addView(mProgressBar);
        }
    }

    private void addEmptyTv() {
        mRootView.removeAllViews();
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        TextView emptyTv = new TextView(this);
        emptyTv.setLayoutParams(params);
        emptyTv.setText(R.string.no_results);
        mRootView.addView(emptyTv);
    }

    private void addRecyclerView(final List<?> contentList) {
        RecyclerView.Adapter<?> adapter = null;

        if (contentList.get(0) instanceof Music) {
            adapter = new MusicAdapter((List<Music>) contentList);
        } else if (contentList.get(0) instanceof Album) {
            adapter = new AlbumAdapter((List<Album>) contentList);
        } else if (contentList.get(0) instanceof Artist) {
            adapter = new ArtistAdapter((List<Artist>) contentList);
        }

        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = new RecyclerView(this);

        if (!(contentList.get(0) instanceof Music)) {
            if (contentList.size() > 1) {
                DividerItemDecoration decoration = new DividerItemDecoration(this,
                        layoutManager.getOrientation());
                recyclerView.addItemDecoration(decoration);
            }
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutParams(params);
        mRootView.removeAllViews();
        mRootView.addView(recyclerView);

    }

    private void handleSearchIntent(String query) {
        SpotifyService service = new SpotifyService.SpotifyBuilder().build();

        if (ACTION.equals(MainActivity.ARTIST_ACTION)) {
            Call<ArtistSearch> call = service.searchArtists(query);
            Callback<ArtistSearch> callback = new Callback<ArtistSearch>() {
                @Override
                public void onResponse(Call<ArtistSearch> call, Response<ArtistSearch> response) {
                    ArtistSearch artistSearch = response.body();

                    if (artistSearch != null && response.isSuccessful()) {
                        List<Artist> artistList = artistSearch.getSearchResult().getResultList();

                        if (artistList.size() >= 1) {
                            addRecyclerView(artistList);
                        } else {
                            addEmptyTv();
                        }
                    } else {
                        Toast.makeText(SearchActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ArtistSearch> call, Throwable t) {
                    Toast.makeText(SearchActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

            call.enqueue(callback);
        } else if (ACTION.equals(MainActivity.ALBUM_ACTION)) {
            Call<AlbumSearch> call = service.searchAlbums(query);
            Callback<AlbumSearch> callback = new Callback<AlbumSearch>() {
                @Override
                public void onResponse(Call<AlbumSearch> call, Response<AlbumSearch> response) {
                    AlbumSearch albumSearch = response.body();
                    if (albumSearch != null && response.isSuccessful()) {
                        List<Album> albumList = albumSearch.getSearchResult().getResultList();

                        if (albumList.size() >= 1) {
                            addRecyclerView(albumList);
                        } else {
                            addEmptyTv();
                        }

                    } else {
                        Toast.makeText(SearchActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AlbumSearch> call, Throwable t) {
                    Toast.makeText(SearchActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

            call.enqueue(callback);
        } else if (ACTION.equals(MainActivity.MUSIC_ACTION)) {
            Call<MusicSearch> call = service.searchMusics(query);

            Callback<MusicSearch> callback = new Callback<MusicSearch>() {
                @Override
                public void onResponse(Call<MusicSearch> call, Response<MusicSearch> response) {
                    MusicSearch musicSearch = response.body();

                    if (musicSearch != null && response.isSuccessful()) {
                        List<Music> musicList = musicSearch.getSearchResult().getResultList();

                        if (musicList.size() >= 1) {
                            addRecyclerView(musicList);
                        } else {
                            addEmptyTv();
                        }
                    } else {
                        Toast.makeText(SearchActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MusicSearch> call, Throwable t) {
                    Toast.makeText(SearchActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

            call.enqueue(callback);
        }
    }
}
