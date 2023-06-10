package com.example.bmshop.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bmshop.Interface.CallBackRecycleview;
import com.example.bmshop.Model.Item;
import com.example.bmshop.R;

import java.util.List;

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.NewHolder>{
    List<Item> itemList;
    CallBackRecycleview callBackRecycleview;
    public NewAdapter(List<Item> itemList, CallBackRecycleview callBackRecycleview){
        this.itemList = itemList;
        this.callBackRecycleview = callBackRecycleview;
    }
    @NonNull
    @Override
    public NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleviewnew,parent,false);
        return new NewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvCost.setText(String.valueOf(item.getCost())+"k");
        if(item.getFlashSale().isIs()){
            holder.imgFS.setVisibility(View.VISIBLE);
            @SuppressLint("ResourceType") Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.drawable.flash);
            holder.imgFS.startAnimation(animation);
        }else{
            holder.imgFS.setVisibility(View.GONE);
        }
        Glide.with(holder.itemView.getContext())
                .load(item.getImg())
                .into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBackRecycleview.setDate(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class NewHolder extends RecyclerView.ViewHolder{
        ImageView img,imgFS;
        TextView tvName,tvCost;
        public NewHolder(@NonNull View itemView) {
            super(itemView);
            tvCost = itemView.findViewById(R.id.tvCost);
            tvName = itemView.findViewById(R.id.tvName);
            img = itemView.findViewById(R.id.img);
            imgFS = itemView.findViewById(R.id.imgFS);
        }
    }
}
