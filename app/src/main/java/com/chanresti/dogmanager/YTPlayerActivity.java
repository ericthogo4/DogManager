package com.chanresti.dogmanager;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class YTPlayerActivity extends YouTubeBaseActivity implements OnInitializedListener {

    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_yt_player);

        youTubePlayerView = (YouTubePlayerView)findViewById(R.id.ap_you_tube_player_view);
        youTubePlayerView.initialize(YouTubeConnector.API_KEY, this);

        TextView youTubeVideoTitle = (TextView)findViewById(R.id.ap_you_tube_video_title_tvw);

        Spanned parsedTitle = Html.fromHtml(getIntent().getStringExtra("you_tube_video_title"));

        youTubeVideoTitle.setText(parsedTitle);

    }

    @Override
    public void onInitializationFailure(Provider provider,
                                        YouTubeInitializationResult result) {
        Toast.makeText(this, getString(R.string.ytp_initialization_failure_text), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player,
                                        boolean restored) {
        if(!restored){
            player.loadVideo(getIntent().getStringExtra("you_tube_video_id"));
        }
    }
}
