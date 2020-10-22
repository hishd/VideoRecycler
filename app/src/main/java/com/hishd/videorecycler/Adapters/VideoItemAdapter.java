package com.hishd.videorecycler.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hishd.videorecycler.Model.VideoItem;
import com.hishd.videorecycler.R;

import java.io.File;
import java.util.List;

public class VideoItemAdapter extends RecyclerView.Adapter<VideoItemAdapter.ViewHolder> {

    Context context;
    List<VideoItem> videoItemList;
    VideoItem videoItem;
    OnVideoItemClickedListener listener;

    public VideoItemAdapter(Context context, List<VideoItem> videoItemList, OnVideoItemClickedListener listener) {
        this.context = context;
        this.videoItemList = videoItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_single_video, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        videoItem = videoItemList.get(position);
        Glide.with(context).asBitmap().load(Uri.fromFile(new File(videoItem.getImagePath()))).into(holder.imgVideoThumbnail);
        holder.txtVidName.setText(videoItem.getName());
        holder.txtVidSize.setText(videoItem.getSize());
    }

    @Override
    public int getItemCount() {
        return videoItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnVideoItemClickedListener listener;
        ImageView imgVideoThumbnail;
        TextView txtVidName;
        TextView txtVidSize;
        Button btnPlay;

        public ViewHolder(@NonNull View itemView, OnVideoItemClickedListener listener) {
            super(itemView);
            this.listener = listener;
            imgVideoThumbnail = itemView.findViewById(R.id.imgVideoThumbnail);
            txtVidName = itemView.findViewById(R.id.txtVidName);
            txtVidSize = itemView.findViewById(R.id.txtVidSize);
            btnPlay = itemView.findViewById(R.id.btnPlay);
            btnPlay.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listener.onVideoClicked(getAdapterPosition());
        }
    }

    public interface OnVideoItemClickedListener {
        void onVideoClicked(int position);
    }
}
