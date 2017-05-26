package www.zemoso.com.openglwithtexture.activities;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

import www.zemoso.com.openglwithtexture.R;
import www.zemoso.com.openglwithtexture.custom_views.MyGLTextureView;

public class MainActivity extends AppCompatActivity implements ExtractorMediaSource.EventListener {

    private DefaultBandwidthMeter defaultBandwidthMeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextureView textureView = (TextureView) findViewById(R.id.texture_view);
        ConstraintLayout parent = (ConstraintLayout) textureView.getParent();
        SimpleExoPlayer mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, getTrackSelector());
        mExoPlayer.setVideoTextureView(textureView);
        mExoPlayer.prepare(getMediaSource("IKEA.mp4", new Handler()));
        mExoPlayer.setPlayWhenReady(true);
        TextureView commentView = new MyGLTextureView(this);
        ConstraintLayout.LayoutParams commentParams = new ConstraintLayout.LayoutParams(600, 600);
        commentParams.setMargins(40, 40, 40, 40);
        parent.addView(commentView);
        commentView.setLayoutParams(commentParams);
        SimpleExoPlayer mExoPlayerComment = ExoPlayerFactory.newSimpleInstance(this, getTrackSelector());
        mExoPlayerComment.setVideoTextureView(commentView);
        mExoPlayerComment.prepare(getMediaSource("redhead.mp4", new Handler()));
        mExoPlayerComment.setPlayWhenReady(true);
    }

    private MediaSource getMediaSource(String videoPath, Handler mainHandler) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getResources().getString(R.string.app_name)), (TransferListener<? super DataSource>) getDefaultBandwidthMeter());
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        Uri videoFile = Uri.parse("file:///android_asset/" + videoPath);
        return new LoopingMediaSource(new ExtractorMediaSource(videoFile,
                dataSourceFactory, extractorsFactory, mainHandler, this));
    }

    private TrackSelector getTrackSelector() {
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(getDefaultBandwidthMeter());
        return new DefaultTrackSelector(videoTrackSelectionFactory);
    }

    private BandwidthMeter getDefaultBandwidthMeter() {
        if (defaultBandwidthMeter == null) {
            defaultBandwidthMeter = new DefaultBandwidthMeter();
        }
        return defaultBandwidthMeter;
    }

    @Override
    public void onLoadError(IOException error) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
