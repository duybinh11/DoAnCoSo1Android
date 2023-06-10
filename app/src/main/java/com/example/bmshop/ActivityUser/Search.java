package com.example.bmshop.ActivityUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.bmshop.Adapter.SearchAdapter;
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

public class Search extends AppCompatActivity {
    SearchView searchView;
    RecyclerView rccvSearch;
    List<Item> itemList;
    SearchAdapter searchAdapter;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        anhXa();
        getData();
        initRccvSearch();
        onClickSearchView();
    }
    public void anhXa(){
        searchView = findViewById(R.id.searchView);
        rccvSearch = findViewById(R.id.rccvSearch);
        itemList = new ArrayList<>();
        searchAdapter = new SearchAdapter(itemList);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang Tải");
    }
    private void getData(){
        progressDialog.show();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_item");
        Query query = mData.orderByChild("date");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Item item = snapshot1.getValue(Item.class);
                    itemList.add(0,item);
                }
                searchAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }
    public void initRccvSearch(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Search.this);
        rccvSearch.setLayoutManager(linearLayoutManager);
        rccvSearch.setAdapter(searchAdapter);
    }
    private void onClickSearchView(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}