package com.example.bmshop.ActivityUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
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

public class TypeActivity extends AppCompatActivity {
    TextView tvType;
    RecyclerView rccvType;
    String type;
    List<Item> itemList;
    NewAdapter newAdapter;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        type = getIntent().getStringExtra("type");
        anhXa();
        getItem();
        setData();
    }
    private void anhXa(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang Tải");
        tvType = findViewById(R.id.tvType);
        rccvType = findViewById(R.id.rccvType);
        itemList = new ArrayList<>();
        newAdapter = new NewAdapter(itemList, new CallBackRecycleview() {
            @Override
            public void setDate(Item item) {
                Intent intent = new Intent(TypeActivity.this,ItemSelected.class);
                intent.putExtra("item",item);
                startActivity(intent);
            }
        });
    }
    private void getItem(){
        progressDialog.show();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_item");
        Query query = null;
        if(!type.equals("Flash Sale")){
            query = mData.orderByChild("type").equalTo(type);
        }else{
            query = mData.orderByChild("flashSale/is").equalTo(true);
        }
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Item item = snapshot1.getValue(Item.class);
                    itemList.add(item);
                }
                newAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(TypeActivity.this, "loi", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setData(){
        tvType.setText(type);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        rccvType.setLayoutManager(gridLayoutManager);
        rccvType.setAdapter(newAdapter);
    }
}