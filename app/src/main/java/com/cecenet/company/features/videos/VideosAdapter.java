package com.cecenet.company.features.videos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cecenet.company.R;
import com.cecenet.company.tools.DefaultString;

import java.util.ArrayList;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.MyView> {
    private final Context context;
    private final ArrayList<Videos> videosArrayList;
    private final VideosActivity videosActivity;

    public VideosAdapter(Context context, ArrayList<Videos> videosArrayList) {
        this.context            = context;
        this.videosArrayList    = videosArrayList;
        this.videosActivity     = (VideosActivity)context;
    }

    public static class MyView extends RecyclerView.ViewHolder {
        ImageView IVListVideo, IVSelectedVideo;

        public MyView(@NonNull View itemView) {
            super(itemView);

            IVListVideo     = itemView.findViewById(R.id.IVListVideo);
            IVSelectedVideo = itemView.findViewById(R.id.IVSelectedVideo);
        }
    }

    @NonNull
    @Override
    public VideosAdapter.MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyView(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_video_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideosAdapter.MyView holder, int position) {
        String name = videosArrayList.get(position).getName();
        String url  = DefaultString.URL_VIDEOS + name;

        holder.IVSelectedVideo.setVisibility(View.GONE);
        Glide.with(context).asBitmap().apply(new RequestOptions().fitCenter().override(200)).load(url)
                .placeholder(R.drawable.img_videos).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.IVListVideo);

        holder.itemView.setOnLongClickListener(v -> {
            holder.IVSelectedVideo.setVisibility(View.VISIBLE);
            selection(holder, videosArrayList.get(position).getVisible().equals("Yes"));

            return true;
        });

        holder.itemView.setOnClickListener(v -> {
            if(videosActivity.selectPosition != -1){
                if(holder.IVSelectedVideo.getVisibility() == View.VISIBLE){
                    holder.IVSelectedVideo.setVisibility(View.GONE);
                    videosActivity.selectPosition = -1;
                    videosActivity.selectionVideo(videosArrayList.get(position).getVisible().equals("Yes"), videosActivity.selectPosition);
                }
                else{
                    holder.IVSelectedVideo.setVisibility(View.VISIBLE);
                    selection(holder, videosArrayList.get(position).getVisible().equals("Yes"));
                }
            }
            else{
                playVideo(holder, name, url);
            }
        });
    }

    private void selection(VideosAdapter.MyView holder, boolean visible){
        if(videosActivity.selectPosition != holder.getAdapterPosition()){
            notifyItemChanged(videosActivity.selectPosition);
            videosActivity.selectPosition = holder.getAdapterPosition();
        }

        videosActivity.selectionVideo(visible, videosActivity.selectPosition);
    }

    private void playVideo(VideosAdapter.MyView holder, String name, String url){
        holder.itemView.setTransitionName("PlayVideo");

        ActivityOptionsCompat AOC   = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, holder.itemView, "PlayVideo");
        Intent intentPlayVideo      = new Intent(context, VideosPlay.class).putExtra("VideoName", name).putExtra("VideoPath", url);

        context.startActivity(intentPlayVideo, AOC.toBundle());
    }

    @Override
    public int getItemCount() {
        return videosArrayList.size();
    }
}