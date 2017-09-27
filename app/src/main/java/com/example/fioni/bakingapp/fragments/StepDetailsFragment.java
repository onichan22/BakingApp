package com.example.fioni.bakingapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fioni.bakingapp.R;
import com.example.fioni.bakingapp.utilities.BakingJSonUtil;
import com.example.fioni.bakingapp.utilities.NetworkUtils;
import com.example.fioni.bakingapp.utilities.Step;
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

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by fioni on 9/7/2017.
 */

public class StepDetailsFragment extends Fragment {
    private static final String STEP_ARRAY_KEY = "ARRAY STEP";
    public Step mStep;
    public int mNextStep;
    public String mRecipeId;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private ImageView mImageView;
    public ArrayList<Step> mStepsArray;
    public TextView details_tv;
    private static final String STEP_KEY = "THIS STEP";
    // private ClickToListen mCallback;
    private TextView nextButton;
    private TextView prevButton;
    public Context mContext;

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
            mStepsArray = savedInstanceState.getParcelableArrayList(STEP_ARRAY_KEY);
            setStepId(Integer.parseInt(mStep.getId()));
            setupViews();

            //mRecipeId = mStep.getR_id();
        } else if (savedInstanceState == null) {
            URL recipeSearchUrl = NetworkUtils.buildUrl();
            new QueryStepsTask().execute(recipeSearchUrl);

        }

        return rootView;
    }

    public void setupViews() {
        if (mNextStep > mStepsArray.size()) {
            mNextStep = mStepsArray.size();
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

    public class QueryStepsTask extends AsyncTask<URL, Void, ArrayList<Step>> {

        @Override
        protected ArrayList<Step> doInBackground(URL... params) {
            URL searchUrl = params[0];
            String recipeSearchResults;
            ArrayList<Step> StepArrayList;
            try {
                recipeSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                StepArrayList = BakingJSonUtil.getStepsFromJson(getActivity(), recipeSearchResults, Integer.parseInt(mRecipeId));

                return StepArrayList;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<Step> steps) {
            mStepsArray = steps;
            if (mNextStep >= 0 && mNextStep < mStepsArray.size()) {
                if (mNextStep < 0) {
                    mNextStep = 0;
                }
                if (mNextStep > mStepsArray.size()) {
                    mNextStep = mStepsArray.size();
                }
                mStep = new Step(
                        mStepsArray.get(mNextStep).getR_id(),
                        mStepsArray.get(mNextStep).getId(),
                        mStepsArray.get(mNextStep).getShort_desc(),
                        mStepsArray.get(mNextStep).getDesc(),
                        mStepsArray.get(mNextStep).getVideo_url(),
                        mStepsArray.get(mNextStep).getThumb_url()
                );

                setupViews();
            }
        }
        }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STEP_KEY, mStep);
        outState.putParcelableArrayList(STEP_ARRAY_KEY, mStepsArray);
        super.onSaveInstanceState(outState);
    }


}


