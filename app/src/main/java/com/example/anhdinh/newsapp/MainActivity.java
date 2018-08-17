package com.example.anhdinh.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {
    // given URL modified to have author's name by adding show-tag=contributor
    private static final String REQUEST_URL = "https://content.guardianapis.com/search?";

    // Create Id for Loader
    private static final int LOADER_ID = 1;

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ArticleAdapter mAdapter;

    // Create TextView variable for empty state
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView articleListView = (ListView) findViewById(R.id.list);

        // Empty State
        mEmptyStateTextView = findViewById(R.id.empty_view);
        articleListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());
        articleListView.setAdapter(mAdapter);

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the position of current article
                Article currentArticle = mAdapter.getItem(position);

                // Convert the String URL into URI Object (to pass into intent constructor)
                Uri articleUri = Uri.parse(currentArticle.getmUrl());

                // Create intent to open web browser
                Intent webIntent = new Intent(Intent.ACTION_VIEW,articleUri);

                startActivity(webIntent);

            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // if there's internet connection, fetch data
        if(networkInfo != null && networkInfo.isConnected()){
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(LOADER_ID, null,this);
        } else {
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }



    }

    // Override Loader Methods
    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Retrieve the strings from preferences
        String articlesCategory = sharedPreferences.getString(getString(R.string.settings_category_key),getString(R.string.settings_category_default));

        // Retrieve strings (entries) from the order-by preference
        String articleOrderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key),getString(R.string.settings_order_by_default));

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // append the Query Parameter
        // For the scope of this project, users can type in "business", "economy", "debate", "government", etg.
        uriBuilder.appendQueryParameter("q",articlesCategory); // this takes in input from user's typing
        uriBuilder.appendQueryParameter("tag","politics/politics");
        uriBuilder.appendQueryParameter("from-date","2014-01-01");
        uriBuilder.appendQueryParameter("orderby",articleOrderBy); // this takes in input from users's choice from list
        uriBuilder.appendQueryParameter("show-tags","contributor");
        uriBuilder.appendQueryParameter("api-key","3f7995ee-b05c-4681-8c5f-afc659879a02");
        Log.v("Main","URL is: " + uriBuilder.toString());

        return new ArticleLoader(this,uriBuilder.toString());


    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        // Set empty state text to display "No data dound"
        mEmptyStateTextView.setText(R.string.no_data_found);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Articles}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (articles!= null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        } else {
            mEmptyStateTextView.setText(R.string.no_data_found);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    // This method initialize the contents of the Activity's options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the option menu we specified in XML
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // get id of the selected item from Option Menu
        int id = item.getItemId();
        // Open SettingActivity when user click on "Settings" item
        if (id == R.id.action_settings){
            Intent settingsIntent = new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
