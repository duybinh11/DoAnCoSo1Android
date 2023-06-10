package com.example.bmshop.fragmentAdmin;

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

import com.example.bmshop.Adapter.StateAdapter;
import com.example.bmshop.Model.ItemState;
import com.example.bmshop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DaBanFragment extends Fragment {
    RecyclerView rccvDaBan;
    List<ItemState> itemStateList;
    StateAdapter stateAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_da_ban, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anhXa(view);
        getData();
        initAdapter();
    }
    private void anhXa(View view){
        rccvDaBan = view.findViewById(R.id.rccvDaBan);
        itemStateList = new ArrayList<>();
        stateAdapter = new StateAdapter(itemStateList);
    }
    private void getData(){
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_da_mua");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    for(DataSnapshot snapshot2 : snapshot1.getChildren()){
                        ItemState itemState = snapshot2.getValue(ItemState.class);
                        itemStateList.add(itemState);
                    }
                }
                stateAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "loi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initAdapter(){
        rccvDaBan.setLayoutManager(new LinearLayoutManager(getContext()));
        rccvDaBan.setAdapter(stateAdapter);
    }
}