package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.model.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {

    private static final String YOUTUBE_API_KEY = "";
    public static final String BASE_API_REQUEST = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    //Example ending to API Request
    //209112/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed


    TextView tvOverview;
    TextView tvTitle;
    TextView tvReleaseDate;
    RatingBar ratingBar;
    YouTubePlayerView youtubePlayer;
    String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvOverview = findViewById(R.id.tvOverview);
        tvTitle = findViewById(R.id.tvTitle);
        ratingBar = findViewById(R.id.ratingBar);
        tvReleaseDate = findViewById(R.id.tvDate);
        youtubePlayer = findViewById(R.id.player);
        final Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(BASE_API_REQUEST, movie.getId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if(results.length() == 0){
                        return;
                    }
                    String video_URL = results.getJSONObject(0).getString("key");
                    Log.d(TAG, video_URL);
                    if(movie.getRating() >= 5){
                        initializePopular(video_URL);
                    } else {
                        initializeUnpopular(video_URL);
                    }

                    //JSONArray results = json.jsonObject.getJSONArray("results");
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse JSON");
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String respons, Throwable throwable){

            }
        });

        //String title = getIntent().getStringExtra("title");
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating((float)movie.getRating());
        tvReleaseDate.setText("Release Date: " + movie.getReleaseDate() +  (movie.getLanguage() == "en" ? "  |    Language: English" : " "));
    }

    private void initializePopular(final String video_url) {
        youtubePlayer.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d( TAG, "onSuccess");
                youTubePlayer.loadVideo(video_url);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d( TAG, "onFailure");
            }
        });
    }

    private void initializeUnpopular(final String video_url) {
        youtubePlayer.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d( TAG, "onSuccess");
                youTubePlayer.cueVideo(video_url);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d( TAG, "onFailure");
            }
        });
    }
}
