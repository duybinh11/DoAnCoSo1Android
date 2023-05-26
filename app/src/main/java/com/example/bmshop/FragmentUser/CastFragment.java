package com.example.bmshop.FragmentUser;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.bmshop.ActivityUser.DetailCast;
import com.example.bmshop.Adapter.CastAdapter;
import com.example.bmshop.Interface.CallBackCast;
import com.example.bmshop.Model.Item;
import com.example.bmshop.Model.ItemCast;
import com.example.bmshop.Model.ItemState;
import com.example.bmshop.R;
import com.google.firebase.auth.FirebaseAuth;
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


public class CastFragment extends Fragment {
    FirebaseDatabase database;
    List<ItemState> itemStateList;
    ProgressDialog progressDialog;
    Button btnBuy;
    RecyclerView rccv;
    ItemCast itemCast;
    FirebaseAuth  mAuth = FirebaseAuth.getInstance();
    CastAdapter castAdapter;
    List<Item> itemList = new ArrayList<>();
    String idUser = mAuth.getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anhXa(view);
        setData();
        initAdapter();
        onClickBuy();
    }
    private void anhXa(View view){
        btnBuy = view.findViewById(R.id.btnBuy);
        rccv = view.findViewById(R.id.rccvCast);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang Tải");
        database = FirebaseDatabase.getInstance();
        itemCast = new ItemCast(idUser,itemList);
        itemStateList = new ArrayList<>();
        castAdapter = new CastAdapter(itemCast.getItemList(), new CallBackCast() {
            @Override
            public void setDate(Item item, Boolean cb) {
                toggleSelection(item,cb);
            }
        });
    }
    private void setData(){
        progressDialog.show();
        DatabaseReference mData = database.getReference("List_cast/"+idUser);
        Query query = mData.orderByChild("date");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemCast.getItemList().clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Item item = snapshot1.getValue(Item.class);
                    itemCast.getItemList().add(0,item);
                }
                progressDialog.dismiss();
                castAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "loi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initAdapter(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rccv.setLayoutManager(linearLayoutManager);
        rccv.setAdapter(castAdapter);
    }
    public void toggleSelection(Item item,boolean cb) {
        String idUser = mAuth.getUid();
        DatabaseReference mData = database.getReference();
        String idVanChuyen = mData.push().getKey();
        String time = date();
        ItemState itemState = new ItemState(idUser,idVanChuyen,time,item,"Chuẩn Bị Hàng");
        if(cb){
            itemStateList.add(itemState);
        }else{
            for(int i=0;i<itemStateList.size();i++){
                if(itemStateList.get(i).getItem().getName().equals(item.getName())){
                    itemStateList.remove(i);
                }
            }
        }
        setVisibleBtn(itemStateList);
    }
    private String date(){
        Date now = new Date();
        String time = "";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            time = now.toInstant().toString();
        }
        return time;
    }
    private void setVisibleBtn(List<ItemState> itemStateList){
        if(itemStateList.size() >0 ){
            btnBuy.setVisibility(View.VISIBLE);
        }else{
            btnBuy.setVisibility(View.GONE);
        }
    }
    private void onClickBuy(){
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyItem();
            }
        });
    }
    private void buyItem(){
        progressDialog.show();
        String idUser = mAuth.getUid();
        DatabaseReference mData = database.getReference("List_state/"+idUser);
        for(ItemState itemState :itemStateList){
            mData.child(itemState.getIdVanChuyen()).child(itemState.getItem().getId()).setValue(itemState);
            deleteItemCast(itemState);
        }
        progressDialog.dismiss();
    }
    private void deleteItemCast(ItemState itemState){
        DatabaseReference mData = database.getReference("List_cast/"+itemState.getId()+"/"+itemState.getItem().getId());
        mData.removeValue();
    }

}