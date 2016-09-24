package com.example.eslam_abo_el_fetouh.myimoviesudacity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.example.eslam_abo_el_fetouh.myimoviesudacity.contentprovider.Contract.MovieEntry;
import com.example.eslam_abo_el_fetouh.myimoviesudacity.contentprovider.Contract.ReviewEntry;
import com.example.eslam_abo_el_fetouh.myimoviesudacity.contentprovider.Contract.VideoEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public class Core {

    Context activity;

    public Core(Context activity){
        this.activity = activity;
    }

    //API key
    private String API_Key = "a3c7442222147fdca96411f37aefe445";

    //Domain
    private String Domain = "https://api.themoviedb.org";


    //API calls
    private String get_movie_byid = Domain + "/3/movie/";
    private String all_movies_url = Domain + "/3/discover/movie?api_key=" + API_Key;
    private String movies_pop_asc = Domain + "/3/discover/movie?api_key=" + API_Key + "&&sort_by=popularity.asc";
    private String movies_pop_des = Domain + "/3/discover/movie?api_key=" + API_Key + "&&sort_by=popularity.desc";
    private String movies_vote    = Domain + "/3/discover/movie?api_key=" + API_Key + "&&sort_by=vote_average.desc";

    //Images locations
//    public String small_image_url = "http://image.tmdb.org/t/p/w185";
    public String large_image_url = "http://image.tmdb.org/t/p/w500";
    public String xlarge_image_url = "http://image.tmdb.org/t/p/w780";

    private String connect(String url) throws IOException {
        String data;
        BufferedReader reader;
        URL url1 = new URL(url);
        Log.d("url",url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setConnectTimeout(2000);
        httpURLConnection.connect();

        InputStream inputStream = httpURLConnection.getInputStream();
        StringBuilder stringBuffer = new StringBuilder();
        assert inputStream != null;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine())!=null){
            stringBuffer.append(line).append("/n");
        }
        data = stringBuffer.toString();
        return data;
    }

    public JSONObject getMovies() throws JSONException {
        JSONObject json = null;
        try {
            String response = connect(all_movies_url);
            if (!response.equals("0")){
                json = new JSONObject(response);
            }else {
                Log.d("getMovies", response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        putMoviesDB(json);
        return json;
    }

    public JSONObject getMovieById(int id) throws JSONException {
        JSONObject json = null;
        try {
            String response = connect(get_movie_byid+id+"?api_key=" + API_Key);
            if (!response.equals("0")){
                json = new JSONObject(response);
            }else {
                Log.d("getMovies", response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONObject getMoviesAsc() throws JSONException {
        JSONObject json = null;
        try {
            if (!connect(all_movies_url).equals("0")){
                json = new JSONObject(connect(movies_pop_asc));
            }else {
                Toast.makeText(activity, "connection Error", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public Cursor getMoviesFav() {
        return activity.getContentResolver().query(MovieEntry.CONTENT_URI,null,MovieEntry.COLUMN_FAV + " = ?",new String[]{String.valueOf(1)},null);
    }

    public JSONObject getMoviesDes() throws JSONException {
        JSONObject json = null;
        try {
            if (!connect(all_movies_url).equals("0")){
                json = new JSONObject(connect(movies_pop_des));
            }else {
                Toast.makeText(activity, "connection Error", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONObject getMoviesRate() throws JSONException {
        JSONObject json = null;
        try {
            if (!connect(all_movies_url).equals("0")){
                json = new JSONObject(connect(movies_vote));
            }else {
                Toast.makeText(activity, "connection Error", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(activity, "Connection Error!!", Toast.LENGTH_SHORT).show();
        }
        putMoviesDB(json);
        return json;
    }

    public JSONObject getMovieReview(int id) throws JSONException {
        JSONObject json = null;
        try {
            if (!connect(all_movies_url).equals("0")){
                json = new JSONObject(connect(get_movie_byid+id+"/reviews?api_key=" + API_Key));
            }else {
                Toast.makeText(activity, "connection Error", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        putReviewsDB(json);
        return json;
    }

    public JSONObject getMovieTrailers(int id) throws JSONException {
        JSONObject json = null;
        try {
            if (!connect(all_movies_url).equals("0")){
                json = new JSONObject(connect(get_movie_byid+id+"/videos?api_key=" + API_Key));
            }else {
                Toast.makeText(activity, "connection Error", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        putVideosDB(json);
        return json;
    }

    public void putMoviesDB(JSONObject jsonObject){

        final String movie_list = "results";
        final String movie_id = "id";
        final String movie_title = "title";
        final String movie_image = "poster_path";
        final String movie_poster = "backdrop_path";
        final String movie_overview = "overview";
        final String movie_release_date = "release_date";
        final String movie_vote_rate = "vote_average";
        try{

            JSONArray movies = jsonObject.getJSONArray(movie_list);
            Vector<ContentValues> cVVector = new Vector<>(movies.length());

            for (int i=0; i < movies.length();i++){
                String id,title,image,poster,overview,release_date,fav,vote_rate;
                JSONObject movie = movies.getJSONObject(i);
                id = movie.getString(movie_id);
                title = movie.getString(movie_title);
                image = movie.getString(movie_image);
                poster = movie.getString(movie_poster);
                overview = movie.getString(movie_overview);
                release_date = movie.getString(movie_release_date);
                vote_rate = movie.getString(movie_vote_rate);
                fav = "0";

                ContentValues moviesValues = new ContentValues();

                moviesValues.put(MovieEntry.COLUMN_ID,id);
                moviesValues.put(MovieEntry.COLUMN_FAV,fav);
                moviesValues.put(MovieEntry.COLUMN_TITLE,title);
                moviesValues.put(MovieEntry.COLUMN_IMAGE_PATH,image);
                moviesValues.put(MovieEntry.COLUMN_POSTER_PATH,poster);
                moviesValues.put(MovieEntry.COLUMN_OVERVIEW,overview);
                moviesValues.put(MovieEntry.COLUMN_RELEASE_DATE,release_date);
                moviesValues.put(MovieEntry.COLUMN_VOTE_RATE,vote_rate);

                cVVector.add(moviesValues);
            }

            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = activity.getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, cvArray);
            }

            Log.i(Core.class.getSimpleName(), "Movie adding Complete. " + inserted + " Inserted");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int updateFavoriteDB(int fav,String id){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry.COLUMN_FAV, fav);
        return activity.getContentResolver().update(MovieEntry.CONTENT_URI, contentValues,MovieEntry._ID + "=" + id , null);
    }

    public void putReviewsDB(JSONObject jsonObject){

        final String review_list = "results";
        final String review_id = "id";
        final String review_author = "author";
        final String review_content = "content";
        final String review_movie_id = "id";
        try{
            String movie_id = jsonObject.getString(review_movie_id);
            JSONArray reviews = jsonObject.getJSONArray(review_list);
            Vector<ContentValues> cVVector = new Vector<>(reviews.length());

            for (int i=0; i < reviews.length();i++){
                String id,author,content;
                JSONObject review = reviews.getJSONObject(i);
                id = review.getString(review_id);
                author = review.getString(review_author);
                content = review.getString(review_content);

                ContentValues reviewsValues = new ContentValues();

                reviewsValues.put(ReviewEntry.COLUMN_REVIEW_ID,id);
                reviewsValues.put(ReviewEntry.COLUMN_AUTHOR,author);
                reviewsValues.put(ReviewEntry.COLUMN_CONTENT,content);
                reviewsValues.put(ReviewEntry.COLUMN_MOVIE_ID,movie_id);

                cVVector.add(reviewsValues);
            }

            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                try {
                    inserted = activity.getContentResolver().bulkInsert(ReviewEntry.CONTENT_URI, cvArray);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            Log.i(Core.class.getSimpleName(), "Review adding Complete. " + inserted + " Inserted");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void putVideosDB(JSONObject jsonObject){

        final String video_list = "results";
        final String video_id = "id";
        final String video_key = "key";
        final String video_name = "name";
        final String video_site = "site";
        final String video_movie_id = "id";
        try{
            String movie_id = jsonObject.getString(video_movie_id);
            JSONArray reviews = jsonObject.getJSONArray(video_list);
            Vector<ContentValues> cVVector = new Vector<>(reviews.length());

            for (int i=0; i < reviews.length();i++){
                String id,key,name,site;
                JSONObject review = reviews.getJSONObject(i);
                id = review.getString(video_id);
                key = review.getString(video_key);
                name = review.getString(video_name);
                site = review.getString(video_site);

                ContentValues videosValues = new ContentValues();

                videosValues.put(VideoEntry.COLUMN_VIDEO_ID,id);
                videosValues.put(VideoEntry.COLUMN_NAME,name);
                videosValues.put(VideoEntry.COLUMN_KEY,key);
                videosValues.put(VideoEntry.COLUMN_MOVIE_ID,movie_id);
                videosValues.put(VideoEntry.COLUMN_SITE,site);

                cVVector.add(videosValues);
            }

            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                try {
                    inserted = activity.getContentResolver().bulkInsert(VideoEntry.CONTENT_URI, cvArray);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            Log.i(Core.class.getSimpleName(), "Videos adding Complete. " + inserted + " Inserted");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
