package com.trainman.gifbrowser.gif;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trainman.gifbrowser.R;
import com.trainman.gifbrowser.models.GifModel;
import com.trainman.gifbrowser.supporting_files.CommonValues;

import java.util.ArrayList;

public class GifsGridAdapter extends RecyclerView.Adapter<GifsGridAdapter.GifViewHolder>{

    private Context context;
    private ArrayList<GifModel> models;
    private boolean noClick;
    private ArrayList<String> gifs;

    public GifsGridAdapter(Context context, ArrayList<GifModel> models, ArrayList<String> gifs, boolean noClick) {
        this.context = context;
        this.models = models;
        this.noClick = noClick;
        this.gifs = gifs;
    }

    @NonNull
    @Override
    public GifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gif_grid_single_item_layout,parent,false);
        return new GifViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GifViewHolder holder, final int position) {
        String path;
        if (noClick){
            path = gifs.get(position);
        }else{
            path = models.get(position).getUrl();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonValues.getInstance().setModel(models.get(position));
                    Intent intent = new Intent(context,SingleGif.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
                }
            });
        }
        Glide.with(context).asGif().load(path).placeholder(new ColorDrawable(Color.BLACK))
                .fallback(new ColorDrawable(Color.GRAY)).error(R.drawable.ic_baseline_error_24).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (noClick){
            return gifs.size();
        }else{
            return models.size();
        }
    }

    public static class GifViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public GifViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
