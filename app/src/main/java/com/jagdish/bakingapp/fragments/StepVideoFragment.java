package com.jagdish.bakingapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.jagdish.bakingapp.R;
import com.jagdish.bakingapp.RecipeDetailActivity;
import com.jagdish.bakingapp.data.CurrentStep;
import com.jagdish.bakingapp.data.Step;
import com.jagdish.bakingapp.viewmodel.StepViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepVideoFragment extends Fragment {

    private static final String TAG = StepVideoFragment.class.getName();
    private static final String BUNDLE_PREVIOUS_POSITION = "previousPos";
    private static final String BUNDLE_PLAY_WHEN_READY = "whenPlayerReady";
    private static final String BUNDLE_STATE_CURRENT_WINDOW = "stateCurrentWindow";

    @BindView(R.id.exoplayer_view)
    SimpleExoPlayerView exoplayer_view;

    @BindView(R.id.step_short_desc_text_view)
    TextView step_short_desc_text_view;

    @BindView(R.id.step_desc_text_view)
    TextView step_desc_text_view;

    @BindView(R.id.btnPrev)
    Button btnPrev;

    @BindView(R.id.btnNext)
    Button btnNext;

    @BindView(R.id.buttonPanel)
    View buttonPanel;

    @BindView(R.id.player_container)
    View player_container;

    @BindView(R.id.no_video_container)
    View no_video_container;

    @BindView(R.id.scollViewDescPanel)
    View scollViewDescPanel;


    private SimpleExoPlayer mExoPlayer;
    private ArrayList<Step> mStepList = null;
    private Step mStep = null;
    private int currentIndex;

    private RecipeDetailActivity mParentActivity;
    private View rootView;

    private boolean isTablet;
    private boolean isLandscape;

    // video state variable
    private long previousPosition;
    private boolean playWhenReady = true;
    private int currentWindow;
    StepViewModel stepViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stepViewModel = ViewModelProviders.of(getActivity()).get(StepViewModel.class);

        CurrentStep currentStep = stepViewModel.getSelectedStep().getValue();
        mStep = currentStep.getStep();

        stepViewModel.getSelectedStep().observe(this, new Observer<CurrentStep>() {
            @Override
            public void onChanged(@Nullable CurrentStep currentStep) {
                currentIndex = currentStep.getIndex();
                mStep = currentStep.getStep();
                setCurrentValue();
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isPhoneAndLandscape()) {
            // hide the action bar
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

            scollViewDescPanel.setVisibility(View.GONE);
            buttonPanel.setVisibility(View.GONE);

            // activate full screen mode
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        } else {
            scollViewDescPanel.setVisibility(View.VISIBLE);
            buttonPanel.setVisibility(View.VISIBLE);
        }

        if (getActivity() != null) {
            mParentActivity = (RecipeDetailActivity) getActivity();
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                mStepList = bundle.getParcelableArrayList("stepList");
            }
            currentIndex = mParentActivity.getCurrentStepPos();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.step_video_fragment, container, false);
        ButterKnife.bind(this, rootView);

        String screenType = getResources().getString(R.string.screen_type);
        if (screenType.equalsIgnoreCase("tablet")) {
            isTablet = true;
        } else {
            isTablet = false;
        }
        isLandscape = getResources().getBoolean(R.bool.is_landscape);

        if (savedInstanceState != null) {
            previousPosition = savedInstanceState.getLong(BUNDLE_PREVIOUS_POSITION);
            playWhenReady = savedInstanceState.getBoolean(BUNDLE_PLAY_WHEN_READY);
            currentWindow = savedInstanceState.getInt(BUNDLE_STATE_CURRENT_WINDOW);
        }
        initControls();

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentIndex > 0) {
                    --currentIndex;
                    mParentActivity.setCurrentStepPos(currentIndex);
                    previousPosition = 0L;
                    stepViewModel.selectStep(new CurrentStep(currentIndex, mStepList.get(currentIndex)));
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStepList != null && currentIndex < (mStepList.size() - 1)) {
                    ++currentIndex;
                    mParentActivity.setCurrentStepPos(currentIndex);
                    previousPosition = 0L;
                    stepViewModel.selectStep(new CurrentStep(currentIndex, mStepList.get(currentIndex)));
                }
            }
        });

        return rootView;
    }

    private void initControls() {
        if (isTablet && isLandscape) {
            buttonPanel.setVisibility(View.INVISIBLE);
            step_short_desc_text_view.setVisibility(View.VISIBLE);
            step_desc_text_view.setVisibility(View.VISIBLE);
        } else if (isPhoneAndLandscape()) {
            buttonPanel.setVisibility(View.GONE);
            step_short_desc_text_view.setVisibility(View.GONE);
            step_desc_text_view.setVisibility(View.GONE);
        } else {
            buttonPanel.setVisibility(View.VISIBLE);
            step_short_desc_text_view.setVisibility(View.VISIBLE);
            step_desc_text_view.setVisibility(View.VISIBLE);
        }

    }


    private void setButtonVisibility() {
        // set prev button visibility
        if (isTablet) {
            buttonPanel.setVisibility(View.INVISIBLE);
            return;
        }

        if (currentIndex == 0) {
            btnPrev.setVisibility(View.INVISIBLE);
        } else {
            btnPrev.setVisibility(View.VISIBLE);
        }
        // set next button visibility
        if (currentIndex == (mStepList.size() - 1)) {
            btnNext.setVisibility(View.INVISIBLE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
        }


    }

    private void setCurrentValue() {

        if (mStep != null) {

            setButtonVisibility();
            setPlayer();

            if (mStep.getShortDescription() != null) {
                step_short_desc_text_view.setText(mStep.getShortDescription());
            }
            if (mStep.getDescription() != null) {
                step_desc_text_view.setText(mStep.getDescription());
            }
        }
    }

    private void setPlayer() {

        if (mStep.getVideoURL() != null && !mStep.getVideoURL().equals("")) {

            // visible player container
            no_video_container.setVisibility(View.GONE);
            player_container.setVisibility(View.VISIBLE);

            initializePlayer(Uri.parse(mStep.getVideoURL()));
        } else {

            // hide play container if no video
            player_container.setVisibility(View.GONE);
            no_video_container.setVisibility(View.VISIBLE);
        }

    }


    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer != null && mExoPlayer.getPlayWhenReady()) {
            mExoPlayer.stop();
        }
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            exoplayer_view.setPlayer(mExoPlayer);
        }

        String userAgent = Util.getUserAgent(getContext(), "BakingApp");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                getContext(), userAgent), new DefaultExtractorsFactory(), null, null);

        boolean resetPosition = false;
        if (isTablet) {
            resetPosition = true;
        } else {
            if (previousPosition == 0) {
                resetPosition = true;
                mExoPlayer.setPlayWhenReady(playWhenReady);
            }
        }
        mExoPlayer.prepare(mediaSource, resetPosition, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            setCurrentValue();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            setCurrentValue();
        }
        if (mExoPlayer != null && previousPosition > 0) {
            mExoPlayer.seekTo(currentWindow, previousPosition);
            mExoPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            // release player
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            // release player
            releasePlayer();
        }
    }

    /**
     * Method to release mExoPlayer
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {

            // get the current values to store in savedInstance
            previousPosition = mExoPlayer.getCurrentPosition();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            currentWindow = mExoPlayer.getCurrentWindowIndex();

            // stop and release player
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mExoPlayer != null) {
            previousPosition = mExoPlayer.getCurrentPosition();
            currentWindow = mExoPlayer.getCurrentWindowIndex();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            outState.putLong(BUNDLE_PREVIOUS_POSITION, previousPosition);
            outState.putBoolean(BUNDLE_PLAY_WHEN_READY, playWhenReady);
            outState.putInt(BUNDLE_STATE_CURRENT_WINDOW, currentWindow);
        }
    }

    private boolean isPhoneAndLandscape() {
        return getContext().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE
                && getContext().getResources().getConfiguration().smallestScreenWidthDp < 600;
    }
}
