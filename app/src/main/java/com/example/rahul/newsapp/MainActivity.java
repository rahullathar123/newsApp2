package com.example.rahul.newsapp;

import android.app.DownloadManager;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<NewsData>>, SearchView.OnQueryTextListener{

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String URL_Data = "https://content.guardianapis.com/search?api-key=92cc03bd-08db-43d0-a983-4ad1dc2b9a47&section=";


    /**
     * Constant value for the Newsloader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int News_LOADER_ID = 1;
    //id for second URL
    ProgressBar mProgressBar;
    /**
     * Adapter for the list of newsData
     */
    private RecyclerView recyclerView;
    private static String query;
    private NewsAdapter mAdapter;
    private List<NewsData> news= null;
    //initialize list
    List<NewsData> newsDataList= new ArrayList<>();
    MenuItem searchMenuItem;
    SearchView searchView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Find a reference to the {@link RecyclerView} in the layout
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);//every item has fixed size
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mAdapter = new NewsAdapter(news, this);
        recyclerView.setAdapter(mAdapter);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(News_LOADER_ID, null, this);
    }



    @Override
    public Loader<List<NewsData>> onCreateLoader(int i, Bundle bundle) {
        mProgressBar.setVisibility(View.VISIBLE);
        // Create a new loader for the given URL
        Log.i("URL2",URL_Data +query);
        return new NewsLoader(this, URL_Data +query);

    }


    @Override
    public void onLoadFinished(Loader<List<NewsData>> loader, List<NewsData> news) {
        mProgressBar.setVisibility(View.INVISIBLE);
        // Clear the adapter of previous News data
        recyclerView.getRecycledViewPool().clear();


        // If there is a valid list of {@link NewsData}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty()) {
            mAdapter.addData(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsData>> loader) {
        // Loader reset, so we can clear out our existing data.
        recyclerView.getRecycledViewPool().clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem menuItem =menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(this);


        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query)
    {
        getLoaderManager().restartLoader(1,null,this);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        query = newText;
       newsDataList = new ArrayList<>();
        // make NewsData final to fix crashing app issue
        for (NewsData newsData : newsDataList){

            String name = newsData.getSectionBelong().toLowerCase();
            if(name.contains(newText))
                newsDataList.add(newsData);

        }
        mAdapter.setNews(newsDataList);
        return true;

    }
}
