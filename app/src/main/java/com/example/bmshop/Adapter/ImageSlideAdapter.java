package com.example.bmshop.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bmshop.ActivityUser.ItemSelected;
import com.example.bmshop.Model.Item;
import com.example.bmshop.R;

import java.util.List;

public class ImageSlideAdapter extends RecyclerView.Adapter<ImageSlideAdapter.ImageSlideHolder>{
    List<Item> itemList;
    public ImageSlideAdapter( List<Item> itemList){
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ImageSlideHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slide,parent,false);
        return new ImageSlideHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSlideHolder holder, int position) {
        Item item = itemList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(item.getImg())
                .into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), ItemSelected.class);
                intent.putExtra("item",item);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ImageSlideHolder extends RecyclerView.ViewHolder{
        ImageView img;
        public ImageSlideHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }
}
