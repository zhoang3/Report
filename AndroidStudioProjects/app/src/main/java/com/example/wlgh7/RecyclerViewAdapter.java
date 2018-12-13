package com.example.wlgh7;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private  MovieInfo movieInfo = new MovieInfo();
    private Context mContext;

    public RecyclerViewAdapter(Context context, MovieInfo movieInfo) {
        this.movieInfo = movieInfo;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called");
        Glide.with(mContext)
                .asBitmap()
                .load(movieInfo.movieThumb_img.get(i))
                .into(viewHolder.image);

        viewHolder.imageName.setText(Html.fromHtml(movieInfo.movieTitle.get(i)));
        viewHolder.ratingBar.setRating(Float.parseFloat(movieInfo.movieUserRating.get(i)) / 2);
        viewHolder.movieDetail.setText(Html.fromHtml(movieInfo.moviePubDate.get(i)) + "\n");
        viewHolder.movieDetail.append(Html.fromHtml(movieInfo.movieDirector.get(i)) + "\n");
        viewHolder.movieDetail.append(Html.fromHtml(movieInfo.movieActor.get(i)) + "\n");
        
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                 //검색 결과의 position 번째 링크 주소를 가져온다.
                String site=movieInfo.movielink.get(i);
                //사이트를 띄운다
                Uri uri= Uri.parse(site);
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieInfo.movieTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView imageName;
        RatingBar ratingBar;
        TextView movieDetail;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.image_name);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            movieDetail = itemView.findViewById(R.id.movieDetail);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
