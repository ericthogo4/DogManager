package com.chanresti.dogmanager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import static android.view.View.GONE;


public class DogTrainingFragment extends Fragment {

    private YouTubeAdapter dtaYouTubeAdapter;
    private RecyclerView dtaRecyclerView;
    private ProgressDialog dtaProgressDialog;
    private Handler dtaProgressDialogHandler;
    private List<VideoItem> dogTrainingVideos;
    private LinearLayout dtaESLinearLayout;
    Button dTAEStateButton;


    private AdView dTAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootView = inflater.inflate(R.layout.fragment_dog_training, container, false);

        dTAdView = (AdView) rootView.findViewById(R.id.dt_ad_view);
        AdRequest dTAdRequest = new AdRequest.Builder().build();
        dTAdView.loadAd(dTAdRequest);

        dtaProgressDialog = new ProgressDialog(getActivity());
        dtaRecyclerView = (RecyclerView) rootView.findViewById(R.id.dta_recycler_view);
        dtaProgressDialog.setTitle(getString(R.string.loading));
        dtaProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dtaRecyclerView.setHasFixedSize(true);
        dtaRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dtaProgressDialogHandler = new Handler();
        dtaESLinearLayout = rootView.findViewById(R.id.dta_empty_state_linear_layout);

        dtaProgressDialog.setMessage(getString(R.string.loading_videos));

        fetchDogTrainingVideos();

        dTAEStateButton= rootView.findViewById(R.id.dta_empty_state_retry_button);

        dTAEStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDogTrainingVideos();
            }
        });

        return rootView;
    }

    private void fetchDogTrainingVideos(){
        dtaProgressDialog.show();

        Runnable fetchDTVRunnable = new Runnable() {
            @Override
            public void run() {
                YouTubeConnector dtaYouTubeConnector = new YouTubeConnector();
                dogTrainingVideos = dtaYouTubeConnector.search();
                dtaProgressDialogHandler.post(new Runnable(){
                    public void run(){
                        setUpDtaRecyclerview();

                        if (dtaProgressDialog.isShowing()) {
                            dtaProgressDialog.dismiss();
                        }

                        setDtaEmptyState();
                    }
                });

            }
        };
        Thread fetchDTVThread = new Thread(fetchDTVRunnable);
        fetchDTVThread.start();
        try {
            fetchDTVThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private void setUpDtaRecyclerview(){
        dtaYouTubeAdapter = new YouTubeAdapter(getActivity(), dogTrainingVideos);
        dtaRecyclerView.setAdapter(dtaYouTubeAdapter);
        dtaYouTubeAdapter.notifyDataSetChanged();
    }

    private void setDtaEmptyState(){

        if(dogTrainingVideos == null){

            if(dtaRecyclerView.getVisibility()== View.VISIBLE){
                dtaRecyclerView.setVisibility(GONE);
            }

            if(dtaESLinearLayout.getVisibility()==GONE){
                dtaESLinearLayout.setVisibility(View.VISIBLE);
            }

        }

        else if(dogTrainingVideos.size()==0){

            if(dtaRecyclerView.getVisibility()== View.VISIBLE){
                dtaRecyclerView.setVisibility(GONE);
            }

            if(dtaESLinearLayout.getVisibility()==GONE){
                dtaESLinearLayout.setVisibility(View.VISIBLE);
            }

        }

        else {

            if(dtaESLinearLayout.getVisibility()==View.VISIBLE){
                dtaESLinearLayout.setVisibility(GONE);
            }

            if(dtaRecyclerView.getVisibility()==GONE){
                dtaRecyclerView.setVisibility(View.VISIBLE);
            }

        }

    }

}