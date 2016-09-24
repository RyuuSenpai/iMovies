package com.example.eslam_abo_el_fetouh.myimoviesudacity.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class Provider extends ContentProvider {

    static final int MOVIES = 100;
    static final int REVIEWS = 101;
    static final int VIDEOS = 102;

    private DBHelper mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        mOpenHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(Contract.MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case REVIEWS: {
                retCursor = mOpenHelper.getReadableDatabase().query(Contract.ReviewEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            }
            case VIDEOS: {
                retCursor = mOpenHelper.getReadableDatabase().query(Contract.VideoEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return Contract.MovieEntry.CONTENT_TYPE;
            case REVIEWS:
                return Contract.ReviewEntry.CONTENT_TYPE;
            case VIDEOS:
                return Contract.VideoEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case MOVIES: {
                long _id = db.insert(Contract.MovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = Contract.MovieEntry.BuildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEWS: {
                long _id = db.insert(Contract.ReviewEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = Contract.ReviewEntry.BuildReviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case VIDEOS: {
                long _id = db.insert(Contract.VideoEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = Contract.VideoEntry.BuildVideoUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
//        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnRow;
        if (selection == null) selection = "1";
        switch (match) {
            case MOVIES: {
                returnRow = db.delete(Contract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case REVIEWS: {
                returnRow = db.delete(Contract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case VIDEOS: {
                returnRow = db.delete(Contract.VideoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (returnRow != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
//        db.close();
        return returnRow;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnRow;
        if (selection == null) selection = "1";
        switch (match) {
            case MOVIES: {
                returnRow = db.update(Contract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case REVIEWS: {
                returnRow = db.update(Contract.ReviewEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case VIDEOS: {
                returnRow = db.update(Contract.VideoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (returnRow != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
//        db.close();
        return returnRow;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contract.CONTENT_AUTHORITY;
        matcher.addURI(authority,Contract.PATH_MOVIES,MOVIES);
        matcher.addURI(authority,Contract.PATH_REVIEWS,REVIEWS);
        matcher.addURI(authority,Contract.PATH_VIDEOS,VIDEOS);
        return matcher;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(Contract.MovieEntry.TABLE_NAME, null, value,SQLiteDatabase.CONFLICT_IGNORE);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
