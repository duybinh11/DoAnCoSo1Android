package com.example.bmshop.fragmentAdmin;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bmshop.Adapter.DonHangAdapter;
import com.example.bmshop.Model.ItemState;
import com.example.bmshop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class DonHangFragment extends Fragment {
    RecyclerView rccvDonHang;
    List<ItemState> itemStateList;
    DonHangAdapter donHangAdapter;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_don_hang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rccvDonHang = view.findViewById(R.id.rccvDonHang);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang Tải");
        itemStateList = new ArrayList<>();
        donHangAdapter = new DonHangAdapter(itemStateList);
        getData();
        initAdapter();
    }
    private void getData(){
        progressDialog.show();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_state");
        Query query = mData.orderByChild("date");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemStateList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    for(DataSnapshot snapshot2 : snapshot1.getChildren()){
                        for(DataSnapshot snapshot3 : snapshot2.getChildren()){
                            ItemState itemState = snapshot3.getValue(ItemState.class);
                            itemStateList.add(0,itemState);
                        }
                    }
                }
                donHangAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "loi", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    private void initAdapter(){
        rccvDonHang.setAdapter(donHangAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rccvDonHang.setLayoutManager(linearLayoutManager);
    }
}