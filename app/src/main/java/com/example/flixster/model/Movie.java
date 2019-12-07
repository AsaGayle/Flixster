package com.example.flixster.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {

    String posterPath;
    String title;
    String overview;
    String language;
    String releaseDate;
    int id;
    double rating;
    boolean adult;
    boolean video;


    String BASE_URL = "https://image.tmdb.org/t/p/";
    String POSTER_W3532 = "w342/";

    // Needed for Parceler
    public Movie(){}

    public Movie(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        rating = jsonObject.getDouble("vote_average");
        language = jsonObject.getString("original_language");
        releaseDate = jsonObject.getString("release_date");
        adult = jsonObject.getBoolean("adult");
        video = jsonObject.getBoolean("video");
        id = jsonObject.getInt("id");

    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for(int i = 0; i < movieJsonArray.length(); i++){
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }

        return movies;
    }

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return BASE_URL+POSTER_W3532+posterPath;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getLanguage() {
        return language;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public boolean isAdult() {
        return adult;
    }

    public boolean isVideo() {
        return video;
    }

    public double getRating() {
        return rating;
    }
}
