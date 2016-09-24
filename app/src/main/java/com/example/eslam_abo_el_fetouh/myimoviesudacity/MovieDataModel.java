package com.example.eslam_abo_el_fetouh.myimoviesudacity;

/**
 * Created by root on 11/18/15.
 */
public class MovieDataModel {

    int id;
    String poster_path;
    String release_date;
    String status;
    String tagline;
    String title;
    float vote_average;
    int vote_count;

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    int fav;
    String trailers_youtube;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public String getTrailers_youtube() {
        return trailers_youtube;
    }

    public void setTrailers_youtube(String trailers_youtube) {
        this.trailers_youtube = trailers_youtube;
    }
}
