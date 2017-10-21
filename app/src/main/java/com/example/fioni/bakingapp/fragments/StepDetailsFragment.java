package com.example.fioni.bakingapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fioni.bakingapp.R;
import com.example.fioni.bakingapp.utilities.Global;
import com.example.fioni.bakingapp.utilities.Step;
import com.google.android.exoplayer2.C;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by fioni on 9/7/2017.
 */

public class StepDetailsFragment extends Fragment {
    private static final String STEP_ARRAY_KEY = "ARRAY STEP";
    private static final String STEP_KEY = "THIS STEP";
    private static final String PLAYER_POSITION = "PLAYER POSITION";
    public Step mStep;
    public int mNextStep;
    public String mRecipeId;
    public ArrayList<Step> mStepsArray;
    public TextView details_tv;
    public Context mContext;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private ImageView mImageView;
    private long mPosition;
    private ArrayList<Step> mStepSet = new ArrayList<Step>();

    // private ClickToListen mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        View rootView = inflater.inflate(R.layout.step_details_list, container, false);
        //nextButton = (TextView) rootView.findViewById(R.id.next_step);
        //prevButton = (TextView) rootView.findViewById(R.id.prev_step);
        details_tv = (TextView) rootView.findViewById(R.id.step_details_tv);
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.media_player_view);
        mImageView = (ImageView) rootView.findViewById(R.id.thumbnail_view);

        if (savedInstanceState != null) {
            mStep = savedInstanceState.getParcelable(STEP_KEY);
            //mStepSet = savedInstanceState.getParcelableArrayList(STEP_ARRAY_KEY);
            mPosition = savedInstanceState.getLong(PLAYER_POSITION, C.TIME_UNSET);
            setStepId(Integer.parseInt(mStep.getId()));
            setupViews();
            if (mPosition != C.TIME_UNSET) mExoPlayer.seekTo(mPosition);

            //mRecipeId = mStep.getR_id();
        } else if (savedInstanceState == null) {
            for (int i = 0; i < Global.steps.size(); i++) {
                if (Global.steps.get(i).getR_id().equals(String.valueOf(Global.recipeId - 1))) {
                    mStepSet.add(new Step(Global.steps.get(i).getR_id(),
                            Global.steps.get(i).getId(),
                            Global.steps.get(i).getShort_desc(),
                            Global.steps.get(i).getDesc(),
                            Global.steps.get(i).getVideo_url(),
                            Global.steps.get(i).getThumb_url())
                    );
                }
            }

            if (mNextStep >= 0 && mNextStep < mStepSet.size()) {
                if (mNextStep < 0) {
                    mNextStep = 0;
                }
                if (mNextStep > mStepSet.size()) {
                    mNextStep = mStepSet.size();
                }
                mStep = new Step(
                        mStepSet.get(mNextStep).getR_id(),
                        mStepSet.get(mNextStep).getId(),
                        mStepSet.get(mNextStep).getShort_desc(),
                        mStepSet.get(mNextStep).getDesc(),
                        mStepSet.get(mNextStep).getVideo_url(),
                        mStepSet.get(mNextStep).getThumb_url()
                );

                Global.stepSetSize = mStepSet.size();
                setupViews();
            }

        }

        return rootView;
    }

    public void setupViews() {
        if (mNextStep > mStepSet.size()) {
            mNextStep = mStepSet.size();
        } else if (mNextStep < 0) {
            mNextStep = 0;
        } else {
            if (mStep == null) {
                details_tv.setText("Select Step");
            } else {
                details_tv.setText(mStep.getDesc());
                if (!mStep.getVideo_url().isEmpty()) {
                    mImageView.setVisibility(View.INVISIBLE);
                    initializePlayer(Uri.parse(mStep.getVideo_url()));
                } else if (mStep.getVideo_url().isEmpty() && (!mStep.getThumb_url().isEmpty())) {
                    mPlayerView.setVisibility(View.INVISIBLE);
                    mImageView.setVisibility(View.VISIBLE);
                    Picasso.with(getContext()).load(mStep.getThumb_url()).into(mImageView);
                } else if (mStep.getVideo_url().isEmpty() && (mStep.getThumb_url().isEmpty())) {
                    mImageView.setVisibility(View.VISIBLE);
                    mPlayerView.setVisibility(View.INVISIBLE);
                    mImageView.setImageResource(R.drawable.cupcake);
                }
            }
        }
    }

    private void initializePlayer(Uri mediaUri) {
        if (mediaUri.toString() != "" && mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(mContext, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    mContext, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mExoPlayer != null){
            mPosition = mExoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    public void setStepId(int aStepID) {
        mNextStep = aStepID;
        if(mExoPlayer != null)
        {
            releasePlayer();
        }

    }

    public void setRecipeId(String r_id) {
        mRecipeId = r_id;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEP_KEY, mStep);
        outState.putParcelableArrayList(STEP_ARRAY_KEY, mStepsArray);
        outState.putLong(PLAYER_POSITION, mPosition);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

}


