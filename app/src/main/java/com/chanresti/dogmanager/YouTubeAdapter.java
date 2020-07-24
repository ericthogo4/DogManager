package com.chanresti.dogmanager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class YouTubeAdapter extends RecyclerView.Adapter<YouTubeAdapter.YTViewHolder> {

    private Context yTContext;
    private List<VideoItem> videoList;

    public class YTViewHolder extends RecyclerView.ViewHolder{

        public ImageView videoThumbnail;
        public TextView videoTitle;
        public RelativeLayout videoLayout;

        public YTViewHolder(View view) {
            super(view);
            videoThumbnail = (ImageView) view.findViewById(R.id.yt_video_thumbnail);
            videoTitle = (TextView) view.findViewById(R.id.yt_video_title);
            videoLayout = (RelativeLayout) view.findViewById(R.id.yt_video_layout);
        }
    }

    public YouTubeAdapter(Context yTContext, List<VideoItem> videoList) {
        this.yTContext = yTContext;
        this.videoList = videoList;
    }

    @Override
    public YTViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.yt_video_item, parent, false);

        return new YTViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(YTViewHolder holder, int position) {
        final VideoItem videoItem = videoList.get(position);
        Spanned parsedTitle = Html.fromHtml(videoItem.getTitle());

        holder.videoTitle.setText(parsedTitle);
        Picasso.with(yTContext)
                .load(videoItem.getThumbnailURL())
                .resize(480,270)
                .centerCrop()
                .into(holder.videoThumbnail);

        holder.videoLayout.setOnClickListener(new View.OnClickListener() {

            //onClick method called when the view is clicked
            @Override
            public void onClick(View view) {

                Intent dtaToYouTubePlayerActivityIntent = new Intent(yTContext, YTPlayerActivity.class);

                dtaToYouTubePlayerActivityIntent.putExtra("you_tube_video_id", videoItem.getId());
                dtaToYouTubePlayerActivityIntent.putExtra("you_tube_video_title",videoItem.getTitle());

                dtaToYouTubePlayerActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                yTContext.startActivity(dtaToYouTubePlayerActivityIntent);
            }
        });
    }

    @Override
    public int getItemCount() {

        if(videoList !=null) {
            return videoList.size();
        }

        else {
            return 0;
        }
    }
}
