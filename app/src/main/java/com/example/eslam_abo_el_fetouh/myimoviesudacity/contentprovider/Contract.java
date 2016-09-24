package com.example.eslam_abo_el_fetouh.myimoviesudacity.contentprovider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    public static final String CONTENT_AUTHORITY = "com.example.eslam_abo_el_fetouh.myimoviesudacity";
    public static final Uri BASE_CONTENT = Uri.parse("content://"+ CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_VIDEOS = "videos";

    public static final class MovieEntry implements BaseColumns{
        public static Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_MOVIES).build();
        public static String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static String COLUMN_ID = "id";
        public static String TABLE_NAME = "movies";
        public static String COLUMN_FAV = "fav";
        public static String COLUMN_IMAGE_PATH = "image";
        public static String COLUMN_POSTER_PATH = "poster";
        public static String COLUMN_TITLE = "title";
        public static String COLUMN_OVERVIEW = "overview";
        public static String COLUMN_RELEASE_DATE = "release";
        public static String COLUMN_VOTE_RATE = "vote";

        public static Uri BuildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

    }

    public static final class ReviewEntry implements BaseColumns{
        public static Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_REVIEWS).build();
        public static String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
        public static String TABLE_NAME = "reviews";
        public static String COLUMN_MOVIE_ID = "movie_id";
        public static String COLUMN_REVIEW_ID = "review_id";
        public static String COLUMN_AUTHOR = "author";
        public static String COLUMN_CONTENT = "content";

        public static Uri BuildReviewUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static Uri BuildReviewMoviewUri(String movie){
            return CONTENT_URI.buildUpon().appendPath(movie).build();
        }

    }

    public static final class VideoEntry implements BaseColumns{
        public static Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_VIDEOS).build();
        public static String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEOS;
        public static String TABLE_NAME = "video";
        public static String COLUMN_MOVIE_ID = "movie_id";
        public static String COLUMN_VIDEO_ID = "video_id";
        public static String COLUMN_KEY = "key";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_SITE = "site";

        public static Uri BuildVideoUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static Uri BuildVideoMoviewUri(String movie){
            return CONTENT_URI.buildUpon().appendPath(movie).build();
        }

    }

}
