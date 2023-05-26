package com.example.bmshop.fragmentAdmin;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bmshop.Adapter.NewAdapter;
import com.example.bmshop.Interface.CallBackRecycleview;
import com.example.bmshop.Model.Item;
import com.example.bmshop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ItemFragment extends Fragment {
    List<Item> itemList;
    RecyclerView rccvItem;
    NewAdapter adapter;
    ProgressDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anhXa(view);
        initAdapterItem();
    }
    private void anhXa(View view){
        rccvItem = view.findViewById(R.id.rccvItem);
        itemList = new ArrayList<>();
        adapter = new NewAdapter(itemList, new CallBackRecycleview() {
            @Override
            public void setDate(Item item) {
                Toast.makeText(getContext(), "ok2", Toast.LENGTH_SHORT).show();
            }
        });
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang Tải");
    }
    private void initAdapterItem(){
        setData();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        rccvItem.setAdapter(adapter);
        rccvItem.setLayoutManager(gridLayoutManager);
    }
    private void setData(){
        progressDialog.show();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("List_item");
        Query query = mDatabase.orderByChild("date");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    itemList.add(0,item );
                }
                progressDialog.dismiss();
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "loi", Toast.LENGTH_SHORT).show();
            }
        });
    }


    
    
}