package com.example.fioni.bakingapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    public ArrayList<Step> mStepsArray;
    public TextView details_tv;
    private static final String STEP_KEY = "THIS STEP";
    private ClickToListen mCallback;
    private TextView nextButton;
    private TextView prevButton;
    public Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        View rootView = inflater.inflate(R.layout.step_details_list, container, false);
        nextButton = (TextView) rootView.findViewById(R.id.next_step);
        prevButton = (TextView) rootView.findViewById(R.id.prev_step);

        if (savedInstanceState != null) {
            mStep = savedInstanceState.getParcelable(STEP_KEY);
            mStepsArray = savedInstanceState.getParcelableArrayList(STEP_ARRAY_KEY);
            setStepId(mStep);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.handleClick(v, mStepsArray);
                }
            });

            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.handleClick(v, mStepsArray);
                }
            });
            //mRecipeId = mStep.getR_id();
        } else if (savedInstanceState == null) {
            URL recipeSearchUrl = NetworkUtils.buildUrl();
            new QueryStepsTask().execute(recipeSearchUrl);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.handleClick(v, mStepsArray);
                }
            });

            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.handleClick(v, mStepsArray);
                }
            });
        }

        details_tv = (TextView) rootView.findViewById(R.id.step_details_tv);
        if(mStep == null){
            details_tv.setText("Select Step");
        }
        else {
            details_tv.setText(mStep.getDesc());
            mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.media_player_view);
            initializePlayer(Uri.parse(mStep.getVideo_url()));
        }

        if(!getResources().getBoolean(R.bool.small_screen)){
            nextButton.setVisibility(View.INVISIBLE);
            prevButton.setVisibility(View.INVISIBLE);
        }


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        try {
            mCallback = (ClickToListen) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ClickToListen");
        }
    }

    public void setStepId(Step aStep) {
        mStep = aStep;
        mNextStep = Integer.parseInt(mStep.getId());
        mRecipeId = mStep.getR_id();
        Log.i("recipeid", mStep.getR_id());
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

    public void nextStep(ArrayList<Step> stepArray) {
        mNextStep = mNextStep + 1;
        mStepsArray = stepArray;
        if(mExoPlayer != null)
        {
            releasePlayer();
        }
        //URL recipeSearchUrl = NetworkUtils.buildUrl();
        //new QueryTask().execute(recipeSearchUrl);
        String videoURL = mStepsArray.get(mNextStep).getVideo_url();
        if (videoURL != ""){
            initializePlayer(Uri.parse(videoURL));
        }

        details_tv.setText(mStepsArray.get(mNextStep).getDesc());
    }

    public void prevStep(ArrayList<Step> stepArray) {
        mNextStep = mNextStep - 1;
        mStepsArray = stepArray;
        if (mNextStep < 0){
            mNextStep = 0;
            Toast.makeText(getContext(), "Reached beginning of steps", Toast.LENGTH_SHORT).show();
            return;
        }else{
            if(mExoPlayer != null)
            {
                releasePlayer();
            }
            //URL recipeSearchUrl = NetworkUtils.buildUrl();
            //new QueryTask().execute(recipeSearchUrl);
            String videoURL = mStepsArray.get(mNextStep).getVideo_url();
            if (videoURL != ""){
                initializePlayer(Uri.parse(videoURL));
            }
            details_tv.setText(mStepsArray.get(mNextStep).getDesc());

        }
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
            }
        }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        mStep.setId(mStepsArray.get(mNextStep).getId());
        mStep.setDesc(mStepsArray.get(mNextStep).getDesc());
        mStep.setShort_desc(mStepsArray.get(mNextStep).getShort_desc());
        mStep.setThumb_url(mStepsArray.get(mNextStep).getThumb_url());
        mStep.setVideo_url(mStepsArray.get(mNextStep).getVideo_url());
        outState.putParcelable(STEP_KEY, mStep);
        outState.putParcelableArrayList(STEP_ARRAY_KEY, mStepsArray);
        super.onSaveInstanceState(outState);
    }

    public interface ClickToListen {
        void handleClick(View v, ArrayList<Step> stepArray);
    }

}


