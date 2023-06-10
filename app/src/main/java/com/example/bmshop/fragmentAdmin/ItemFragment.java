package com.example.bmshop.fragmentAdmin;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.example.bmshop.ActivityAdmin.ItemDetail;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        getData();
        initAdapterItem();
    }
    private void anhXa(View view){
        rccvItem = view.findViewById(R.id.rccvItem);
        itemList = new ArrayList<>();
        adapter = new NewAdapter(itemList, new CallBackRecycleview() {
            @Override
            public void setDate(Item item) {
                Intent intent = new Intent(getContext(), ItemDetail.class);
                intent.putExtra("item",item);
                getContext().startActivity(intent);
            }
        });
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang Tải");
    }
    private void initAdapterItem(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        rccvItem.setAdapter(adapter);
        rccvItem.setLayoutManager(gridLayoutManager);
    }
    private void getData(){
        progressDialog.show();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("List_item");
        Query query = mDatabase.orderByChild("date");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    setIsFS(item);
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
    private void setIsFS(Item item){
        Date now = new Date();
        Date end = formatDate(item.getFlashSale().getEnd());
        if(now.getTime()-end.getTime()>0){
            DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_item/"+item.getId());
            mData.child("flashSale").child("is").setValue(false);
        }
        Date start = formatDate(item.getFlashSale().getStart());
        if(now.getTime()-start.getTime()<0){
            DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_item/"+item.getId());
            mData.child("flashSale").child("is").setValue(false);
        }
        if(now.getTime() - end.getTime()<0 && now.getTime() - start.getTime()>0){
            DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_item/"+item.getId());
            mData.child("flashSale").child("is").setValue(true);
        }
    }
    private Date formatDate(String time){
        Date date = null;
        String typeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        SimpleDateFormat format = new SimpleDateFormat(typeFormat);
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }


    
    
}