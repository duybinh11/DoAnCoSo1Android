package com.example.bmshop.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bmshop.ActivityUser.TypeActivity;
import com.example.bmshop.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GriHolder>{
    List<String> nameList = new ArrayList<>();
    List<Integer> imgList = new ArrayList<>();
    public GridAdapter(){
        nameList.add("Thời Trang");
        nameList.add("Đồ Ăn");
        nameList.add("Skincare");
        nameList.add("Điện Tử");
        nameList.add("Gia Dụng");
        nameList.add("Flash Sale");

        imgList.add(R.drawable.thoi_trang);
        imgList.add(R.drawable.food);
        imgList.add(R.drawable.skincare);
        imgList.add(R.drawable.dientu);
        imgList.add(R.drawable.giadung);
        imgList.add(R.drawable.flashsale);
    }
    @NonNull
    @Override
    public GriHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleviewgrid,parent,false);
        return new GriHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GriHolder holder, int position) {
        String name = nameList.get(position);
        holder.tvNameType.setText(name);
        holder.img.setImageResource(imgList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), TypeActivity.class);
                intent.putExtra("type",name);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    public class GriHolder extends RecyclerView.ViewHolder{
        TextView tvNameType;
        ImageView img;
        public GriHolder(@NonNull View itemView) {
            super(itemView);
            tvNameType = itemView.findViewById(R.id.tvNameType);
            img = itemView.findViewById(R.id.img);
        }
    }
}
