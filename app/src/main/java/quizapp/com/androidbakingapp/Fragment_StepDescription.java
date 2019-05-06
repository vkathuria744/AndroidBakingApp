package quizapp.com.androidbakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Fragment_StepDescription extends Fragment {
    @BindView(R.id.mediaPlayer_exo)
    SimpleExoPlayerView simpleExoPlayerView;

    @BindView(R.id.noVideoAvailable_tv)
    TextView noVideoAvailable_tv;

    @BindView(R.id.stepName_tv)
    TextView stepName_tv;

    @BindView(R.id.stepThumbnail_iv)
    ImageView stepThumbnail_iv;

    @BindView(R.id.stepDescription_tv)
    TextView stepDescription_tv;

    @BindView(R.id.nextStep_btn)
    Button nextStep_btn;

    @BindView(R.id.prevStep_btn)
    Button prevStep_btn;

    View rootView;
    private SimpleExoPlayer player;
    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private long playerPosition;
    private BandwidthMeter bandwidthMeter;

    boolean hasVideo;
    String videoURL;
    ArrayList<RecipeStep> steps;
    int index;

    boolean isTwoPane;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_step_description, container, false);

        if (savedInstanceState != null) {
            shouldAutoPlay = savedInstanceState.getBoolean("playerState");
            playerPosition = savedInstanceState.getLong("playerPosition");
        }
        else
        {
            shouldAutoPlay=true;
            playerPosition=0;
        }

        ButterKnife.bind(this, rootView);
        steps = getArguments().getParcelableArrayList("steps");
        index = getArguments().getInt("index");
        isTwoPane = getArguments().getBoolean("isTwoPane");

        RecipeStep recipeStep = steps.get(index);
        if (recipeStep.getVideoURL().length()!=0) {
            hasVideo = true;
            videoURL = recipeStep.getVideoURL();
        }

        else {
            hasVideo = false;
            simpleExoPlayerView.setVisibility(View.INVISIBLE);
            noVideoAvailable_tv.setVisibility(View.VISIBLE);

        }

        if (recipeStep.getThumbnailURL().length()!=0) {
            Uri builtUri1 = Uri.parse(recipeStep.getThumbnailURL()).buildUpon().build();
            Glide.with(getActivity())
                    .asBitmap()
                    .load(builtUri1)
                    .into(stepThumbnail_iv);
        }
        else {
            stepThumbnail_iv.setImageResource(R.drawable.dummy_recipe);
        }
        stepThumbnail_iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        stepName_tv.setText(recipeStep.getShortDescription());
        stepDescription_tv.setText(recipeStep.getDescription());

        if (index == 0 || isTwoPane) {
            prevStep_btn.setVisibility(View.GONE);
        }
        else {
            prevStep_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public  void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Activity_StepDescription.class);
                    intent.putExtra("index", index-1);

                    if (index-1 == 0) {
                        intent.putExtra("step_title", steps.get(index-1).getShortDescription());

                    }
                    else {
                        intent.putExtra("step_title", "Step #"+Integer.toString(index-1));

                    }
                    intent.putParcelableArrayListExtra("steps", steps);
                    startActivity(intent);
                    getActivity().finish();
                }

            });
        }
        if (index == steps.size()-1 || isTwoPane) {
            nextStep_btn.setVisibility(View.GONE);
        }
        else{
            nextStep_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Activity_StepDescription.class);
                    intent.putExtra("index", index+1);
                    intent.putExtra("step_title", "Step #"+Integer.toString(index+1));
                    intent.putParcelableArrayListExtra("steps", steps);
                    startActivity(intent );
                    getActivity().finish();
                }
            });
        }


        if(hasVideo)
        {
            bandwidthMeter = new DefaultBandwidthMeter();
            mediaDataSourceFactory = new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), "mediaPlayerSample"), (TransferListener< ?super DataSource>) bandwidthMeter);
            window = new Timeline.Window();
        }
        return rootView;

    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean("playerState", player.getPlayWhenReady());
        savedInstanceState.putLong("playerPosition", player.getCurrentPosition());
    }

    public boolean isInLandscapeMode()
    {
        return (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);

    }
    private void initializePlayer() {
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        if (isTwoPane) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
            params.height = (int)(400*getResources().getDisplayMetrics().density);
            simpleExoPlayerView.setLayoutParams(params);
        }
        else {
            if(isInLandscapeMode()) {
                View decorView = getActivity().getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
                        params.width = params.MATCH_PARENT;
                        params.height = params.MATCH_PARENT;
                        params.setMargins(0,0,0,0);
                        params.setMarginStart(0);
                        params.setMarginEnd(0);
                        simpleExoPlayerView.setLayoutParams(params);
                        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);



                        stepName_tv.setVisibility(View.GONE);
                        stepThumbnail_iv.setVisibility(View.GONE);
                        stepDescription_tv.setVisibility(View.GONE);
                        nextStep_btn.setVisibility(View.GONE);
                        prevStep_btn.setVisibility(View.GONE);

            }

            else {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = (int)(250*getResources().getDisplayMetrics().density);
                params.topMargin = (int)(20*getResources().getDisplayMetrics().density);
                params.setMarginStart((int)(20*getResources().getDisplayMetrics().density));
                params.setMarginEnd((int)(20*getResources().getDisplayMetrics().density));
                simpleExoPlayerView.setLayoutParams(params);
                stepName_tv.setVisibility(View.VISIBLE);
                stepThumbnail_iv.setVisibility(View.VISIBLE);
                stepDescription_tv.setVisibility(View.VISIBLE);

                if (index!=steps.size()-1)
                    nextStep_btn.setVisibility(View.VISIBLE);
                if (index!=0)
                    prevStep_btn.setVisibility(View.VISIBLE);

            }
        }
        simpleExoPlayerView.requestFocus();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
        simpleExoPlayerView.setPlayer(player);
        player.setPlayWhenReady(shouldAutoPlay);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoURL),
                mediaDataSourceFactory, extractorsFactory, null, null );

        player.prepare(mediaSource);
        player.seekTo(playerPosition);

        }
        private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
        }
        @Override
        public void onStart() {
        super.onStart();
        if (hasVideo) {
            if (Util.SDK_INT > 28) {
                initializePlayer();
            }
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        if (hasVideo) {
            if ((Util.SDK_INT <= 28 || player == null))
            {
                initializePlayer();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (hasVideo) {
            if (Util.SDK_INT <= 28)
            {
                releasePlayer();
            }
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (hasVideo)
        {
            if (Util.SDK_INT > 28)
            {
                releasePlayer();
            }
        }
    }
}
