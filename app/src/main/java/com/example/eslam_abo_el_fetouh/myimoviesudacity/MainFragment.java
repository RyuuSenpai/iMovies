package com.example.eslam_abo_el_fetouh.myimoviesudacity;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.eslam_abo_el_fetouh.myimoviesudacity.contentprovider.Contract.MovieEntry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_UID = 1;
    static final int COL_MOVIE_FAV = 2;
    static final int COL_MOVIE_TITLE = 3;
    static final int COL_MOVIE_IMAGE = 4;
    static final int COL_MOVIE_RELEASE = 5;
    static final int COL_MOVIE_OVERVIEW = 6;
    private static final String[] MOVIE_COLUMNS = {
            MovieEntry.TABLE_NAME + "." + MovieEntry._ID,
            MovieEntry.COLUMN_ID,
            MovieEntry.COLUMN_FAV,
            MovieEntry.COLUMN_TITLE,
            MovieEntry.COLUMN_IMAGE_PATH,
            MovieEntry.COLUMN_POSTER_PATH,
            MovieEntry.COLUMN_RELEASE_DATE,
            MovieEntry.COLUMN_OVERVIEW
    };
    public int id = 0;
    ArrayList<MovieDataModel> movies = new ArrayList<>();
    GridView gridView;
    ImageAdapter imageAdapter;
    private int mLoader;
    private int MOVIES_LOADER = 0;
    private int FAV_LOADER = 1;
    private int HIGH_RATE_LOADER = 2;

    public MainFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_fragment_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imageAdapter = new ImageAdapter(getActivity(), null, 0);
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        movies.clear();
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        gridView = (GridView) view.findViewById(R.id.grid_view);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
        super.onResume();
    }

    @Override
    public void onStart() {
        new loadingData(0).execute();
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_vote) {
            mLoader = HIGH_RATE_LOADER;
            getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
            new loadingData(1).execute();
            return true;
        }
        if (id == R.id.action_desc) {
            mLoader = MOVIES_LOADER;
            getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
            new loadingData(0).execute();
            return true;
        }
        if (id == R.id.action_fav) {
            mLoader = FAV_LOADER;
            getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mLoader == MOVIES_LOADER) {
            return new CursorLoader(getActivity(), MovieEntry.CONTENT_URI, MOVIE_COLUMNS, null, null, MovieEntry._ID + " ASC");
        } else if (mLoader == HIGH_RATE_LOADER) {
            return new CursorLoader(getActivity(), MovieEntry.CONTENT_URI, MOVIE_COLUMNS, null, null, MovieEntry.COLUMN_VOTE_RATE + " DESC");
        } else if (mLoader == FAV_LOADER) {
            return new CursorLoader(getActivity(), MovieEntry.CONTENT_URI, MOVIE_COLUMNS, MovieEntry.COLUMN_FAV + " = ?", new String[]{String.valueOf(1)}, MovieEntry._ID + " ASC");
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        imageAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        imageAdapter.swapCursor(null);
    }

    private class loadingData extends AsyncTask {

        JSONArray results;
        private int orderFlag = 0;

        public loadingData(int orderFlag) {
            this.orderFlag = orderFlag;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            movies.clear();
        }

        @Override
        protected void onPostExecute(Object o) {
            gridView.setAdapter(imageAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    DetailsFragment fragment2 = new DetailsFragment();
                    Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                    fragment2.setID(cursor.getInt(COL_MOVIE_UID));
                    FragmentManager fragmentManager = getFragmentManager();
                    if (getActivity().findViewById(R.id.fragment_pane) != null) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment, fragment2);
                        fragmentTransaction.commit();
                    } else {
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.fragment, fragment2);
//                        fragmentTransaction.addToBackStack(null);
//                        fragmentTransaction.commit();
                        Intent in = new Intent(getActivity(), DetailsActivity.class);
                        in.putExtra("id", cursor.getInt(COL_MOVIE_UID));
                        startActivity(in);
                    }

                }
            });
        }

        @Override
        protected Object doInBackground(Object[] params) {
            Core core = new Core(getActivity());
            try {
                JSONObject data = null;
                if (orderFlag == 0) {
                    data = core.getMovies();
                } else if (orderFlag == 1) {
                    data = core.getMoviesRate();
                } else if (orderFlag == 2) {
                    data = core.getMoviesAsc();
                }
                if (data != null) {
                    results = data.getJSONArray("results");
                    DetailsFragment fragment2 = new DetailsFragment();

                    fragment2.setID(results.getJSONObject(0).getInt("id"));
                    FragmentManager fragmentManager = getFragmentManager();
                    if (getActivity().findViewById(R.id.fragment_pane) != null) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment, fragment2);
                        fragmentTransaction.commit();
                    }

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject movie = results.getJSONObject(i);
                        MovieDataModel m = new MovieDataModel();
                        m.setId(movie.getInt("id"));
                        m.setPoster_path(movie.getString("poster_path"));
                        m.setTitle(movie.getString("title"));
                        Log.d("Movie Title", movie.getString("title"));
                        movies.add(m);
                    }
                    Log.d("data", data.toString());
                }
            } catch (Exception e) {
                String error = "1";
                publishProgress(error);
            }
            return null;
        }
    }

}
