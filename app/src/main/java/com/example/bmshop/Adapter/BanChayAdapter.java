package com.example.bmshop.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bmshop.ActivityUser.ItemSelected;
import com.example.bmshop.Model.Item;
import com.example.bmshop.R;

import java.util.ArrayList;
import java.util.List;

public class BanChayAdapter  extends RecyclerView.Adapter<BanChayAdapter.BanChayHolder>{
    List<Item> itemList;
    public BanChayAdapter(List<Item> itemList){
        this.itemList = itemList;
    }
    @NonNull
    @Override
    public BanChayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleviewbanchay,parent,false);
        return new BanChayHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BanChayHolder holder, int position) {
        Item item = itemList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(item.getImg())
                .into(holder.img);
        holder.tvName.setText(item.getName());
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

    public class BanChayHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView tvName;
        public BanChayHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
