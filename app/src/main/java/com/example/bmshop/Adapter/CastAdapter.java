package com.example.bmshop.Adapter;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bmshop.ActivityUser.DetailCast;
import com.example.bmshop.ActivityUser.ItemSelected;
import com.example.bmshop.Interface.CallBackCast;
import com.example.bmshop.Model.Item;
import com.example.bmshop.Model.ItemCast;
import com.example.bmshop.Model.ItemState;
import com.example.bmshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastHolder>{
    List<Item> itemList;
    CallBackCast callBackCast;

    public CastAdapter(List<Item> itemList, CallBackCast callBackCast){

        this.itemList = itemList;
        this.callBackCast = callBackCast;
    }

    @Override
    public CastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleviewcast,parent,false);
        return new CastHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastHolder holder, int position) {
        Item item = itemList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvSlm.setText(String.valueOf(item.getSlm()));
        holder.tvMoney.setText(String.valueOf(item.getSlm()*item.getCost())+"k");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailCast.class);
                intent.putExtra("item",item);
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                callBackCast.setDate(item,b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class CastHolder extends RecyclerView.ViewHolder{
        CheckBox cb;
        TextView tvName,tvMoney,tvSlm;
        public CastHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            cb = itemView.findViewById(R.id.cb);
            tvMoney = itemView.findViewById(R.id.tvMoney);
            tvSlm = itemView.findViewById(R.id.tvSlm);
        }
    }


}
