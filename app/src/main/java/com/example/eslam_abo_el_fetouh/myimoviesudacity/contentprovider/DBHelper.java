package com.example.eslam_abo_el_fetouh.myimoviesudacity.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.eslam_abo_el_fetouh.myimoviesudacity.contentprovider.Contract.MovieEntry;
import com.example.eslam_abo_el_fetouh.myimoviesudacity.contentprovider.Contract.ReviewEntry;
import com.example.eslam_abo_el_fetouh.myimoviesudacity.contentprovider.Contract.VideoEntry;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance;

    final static String DATABASE_NAME = "movies.db";

    private static int DATABASE_VERSION = 1;

    public static synchronized DBHelper getInstance(Context context)
    {
        if (instance == null) {
            instance = new DBHelper(context);
        }

        return instance;
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +

                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntry.COLUMN_ID + " INTEGER NOT NULL," +
                MovieEntry.COLUMN_FAV + " INTEGER NOT NULL," +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_IMAGE_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                MovieEntry.COLUMN_VOTE_RATE + " REAL NOT NULL," +
                " UNIQUE (" + MovieEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +

                ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                ReviewEntry.COLUMN_MOVIE_ID + " REAL NOT NULL, " +
                ReviewEntry.COLUMN_REVIEW_ID + " REAL NOT NULL," +
                " FOREIGN KEY (" + ReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_ID + "), " +
                " UNIQUE (" + ReviewEntry.COLUMN_REVIEW_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + VideoEntry.TABLE_NAME + " (" +

                VideoEntry._ID + " TEXT PRIMARY KEY," +
                VideoEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                VideoEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                VideoEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                VideoEntry.COLUMN_SITE + " TEXT NOT NULL," +
                VideoEntry.COLUMN_VIDEO_ID + " TEXT NOT NULL," +
                " FOREIGN KEY (" + VideoEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_ID + "), " +
                " UNIQUE (" + VideoEntry.COLUMN_VIDEO_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
        db.execSQL(SQL_CREATE_VIDEO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VideoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        onCreate(db);
    }
}
