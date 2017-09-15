package com.example.fioni.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    public Step mStep;
    public int mNextStep;
    public String mRecipeId;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    public ArrayList<Step> mStepsArray;
    public TextView details_tv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.step_details_list, container, false);

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
            rootView.findViewById(R.id.next_step).setVisibility(View.INVISIBLE);
            rootView.findViewById(R.id.prev_step).setVisibility(View.INVISIBLE);
        }
        URL recipeSearchUrl = NetworkUtils.buildUrl();
        new QueryStepsTask().execute(recipeSearchUrl);
        return rootView;
    }

    public void setStepId(Step aStep) {
        mStep = aStep;
        mNextStep = Integer.parseInt(mStep.getId());
        mRecipeId = mStep.getR_id();
    }

    private void initializePlayer(Uri mediaUri) {
        if(mediaUri != null && mExoPlayer == null){
            Context context = getContext();
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(context, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    context, userAgent), new DefaultExtractorsFactory(), null, null);
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

    public void nextStep() {
        mNextStep = mNextStep + 1;
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

    public void prevStep() {
        mNextStep = mNextStep - 1;

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
    }
