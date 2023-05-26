package com.example.bmshop.FragmentUser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bmshop.Adapter.StateAdapter;
import com.example.bmshop.Model.ItemState;
import com.example.bmshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StateFragment extends Fragment {
    RecyclerView rccvState;
    List<ItemState> itemStateList;
    StateAdapter stateAdapter;
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_state, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anhXa(view);
        setDate();
        initState();
    }
    public void anhXa(View view){
        rccvState = view.findViewById(R.id.rccvState);
        itemStateList = new ArrayList<>();
        stateAdapter = new StateAdapter(itemStateList);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }
    public void setDate(){
        String idUser = mAuth.getUid();
        DatabaseReference mData = database.getReference("List_state/"+idUser);
        Query query = mData.orderByChild("date");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemStateList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    for(DataSnapshot snapshot2 : snapshot1.getChildren()){
                        ItemState itemState = snapshot2.getValue(ItemState.class);
                        itemStateList.add(0,itemState);
                    }
                }
                stateAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Loi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initState(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rccvState.setLayoutManager(linearLayoutManager);
        rccvState.setAdapter(stateAdapter);
    }

}