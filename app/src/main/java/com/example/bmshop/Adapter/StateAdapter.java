package com.example.bmshop.Adapter;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bmshop.ActivityUser.DetailState;
import com.example.bmshop.Model.Item;
import com.example.bmshop.Model.ItemState;
import com.example.bmshop.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateHolder>{
    List<ItemState> itemStateList;
    public StateAdapter(List<ItemState> itemStateList){
        this.itemStateList = itemStateList;
    }

    @NonNull
    @Override
    public StateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleviewstate,parent,false);
        return new StateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StateHolder holder, int position) {
        ItemState itemState = itemStateList.get(position);
        Item item = itemState.getItem();
        Glide.with(holder.itemView.getContext()).load(itemState.getItem().getImg()).into(holder.img);
        holder.tvName.setText(itemState.getItem().getName());
        String time = date(itemState.getDate());
        holder.tvDate.setText(time);
        holder.tvState.setText(itemState.getState());
        int money = 0;
        if(item.getFlashSale().isIs()){
           money = item.getSlm()*item.getCost()-item.getSlm()*item.getCost()*item.getFlashSale().getPercent()/100;
        }else{
            money= item.getSlm()*item.getCost();
        }
        holder.tvMoney.setText(money+"k");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailState.class);
                intent.putExtra("itemState",itemState);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemStateList.size();
    }

    public class StateHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView tvName,tvDate,tvMoney,tvState;
        public StateHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tvName = itemView.findViewById(R.id.tvName);
            tvMoney = itemView.findViewById(R.id.tvMoney);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvState = itemView.findViewById(R.id.tvState);
        }
    }
    private String date(String date){
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        String time = "";
        try {
            Date date1 = input.parse(date);
            time = output.format(date1).toString();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return time;
    }


}
