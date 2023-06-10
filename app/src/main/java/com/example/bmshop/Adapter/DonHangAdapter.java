package com.example.bmshop.Adapter;

import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bmshop.ActivityAdmin.DetailDonHang;
import com.example.bmshop.Model.ItemState;
import com.example.bmshop.Model.User;
import com.example.bmshop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.DonHangHolger>{
    List<ItemState> itemStateList;
    public DonHangAdapter(List<ItemState> itemStateList){
        this.itemStateList = itemStateList;
    }
    @NonNull
    @Override
    public DonHangHolger onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleviewdonhang,parent,false);
        return new DonHangHolger(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangHolger holder, int position) {
        ItemState itemState = itemStateList.get(position);
        setDataUser(itemState.getId(),holder.tvNameUser);
        Glide.with(holder.itemView.getContext()).load(itemState.getItem().getImg()).into(holder.img);
        holder.tvName.setText(itemState.getItem().getName());
        holder.tvState.setText(itemState.getState());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailDonHang.class);
                intent.putExtra("itemState",itemState);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemStateList.size();
    }

    public class DonHangHolger extends RecyclerView.ViewHolder{
        ImageView img;
        TextView tvNameUser,tvName,tvState;
        public DonHangHolger(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tvName = itemView.findViewById(R.id.tvName);
            tvNameUser = itemView.findViewById(R.id.tvNameUser);
            tvState = itemView.findViewById(R.id.tvState);
        }
    }
    private void setDataUser(String id,TextView tvNameUser){
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_user/"+id);
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                tvNameUser.setText(user.getName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
