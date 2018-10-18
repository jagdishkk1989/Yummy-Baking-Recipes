package com.jagdish.bakingapp.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.jagdish.bakingapp.data.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepVideoFragment extends BaseFragment {

    private static final String TAG = StepVideoFragment.class.getName();

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

    SimpleExoPlayer mExoPlayer;
    ArrayList<Step> mStepList = null;
    Step mStep = null;
    int currentPos;

    RecipeDetailActivity mParentActivity;
    View rootView;

    private boolean isTablet;
    private boolean isLandscape;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isPhoneAndLandscape()) {
            // hide the action bar
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

            // activate full screen mode
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() != null) {
            mParentActivity = (RecipeDetailActivity) getActivity();
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                mStepList = bundle.getParcelableArrayList("stepList");
                currentPos = bundle.getInt("currentPos");
            }
        }
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

        initControls();

        setCurrentValue();

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPos > 0) {
                    --currentPos;
                    mParentActivity.setCurrentStepPos(currentPos);
                    setCurrentValue();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStepList != null && currentPos < (mStepList.size() - 1)) {
                    ++currentPos;
                    mParentActivity.setCurrentStepPos(currentPos);
                    setCurrentValue();
                }
            }
        });

        return rootView;
    }

    private void initControls() {
        if (isTablet) {
            buttonPanel.setVisibility(View.GONE);
        } else if (isPhoneAndLandscape()) {
            buttonPanel.setVisibility(View.GONE);
            step_short_desc_text_view.setVisibility(View.GONE);
        }
        if (mStepList != null) {
            setButtonVisibility();
        }
    }


    private void setButtonVisibility() {
        // set prev button visibility
        if (currentPos == 0) {
            btnPrev.setVisibility(View.INVISIBLE);
        } else {
            btnPrev.setVisibility(View.VISIBLE);
        }
        // set next button visibility
        if (currentPos == (mStepList.size() - 1)) {
            btnNext.setVisibility(View.INVISIBLE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
        }

    }

    private void setCurrentValue() {

        if (mStepList != null) {

            setButtonVisibility();

            mStep = mStepList.get(currentPos);

            if (mStep.getVideoURL() != null && !mStep.getVideoURL().equals("")) {
                initializePlayer(Uri.parse(mStep.getVideoURL()));
            }
            if (mStep.getShortDescription() != null) {
                step_short_desc_text_view.setText(mStep.getShortDescription());
            }
            if (mStep.getDescription() != null) {
                step_desc_text_view.setText(mStep.getDescription());
            }
        }
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            exoplayer_view.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) {
            releasePlayer();
        }
    }

    /**
     * Method to release mExoPlayer
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private boolean isPhoneAndLandscape() {
        return getContext().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE
                && getContext().getResources().getConfiguration().smallestScreenWidthDp < 600;
    }

}
