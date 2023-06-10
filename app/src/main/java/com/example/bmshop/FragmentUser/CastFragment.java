package com.example.bmshop.FragmentUser;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CastFragment extends Fragment {
    FirebaseDatabase database;
    List<ItemState> itemStateList;
    ProgressDialog progressDialog;
    TextView tvTitle,tvMoney;
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
        getDataCast();
        initAdapter();
        onClickBuy();
    }
    private void anhXa(View view){
        tvTitle = view.findViewById(R.id.tvTitle);
        tvMoney = view.findViewById(R.id.tvMoney);
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
    private void getDataCast(){
        progressDialog.show();
        DatabaseReference mData = database.getReference("List_cast/"+idUser);
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemCast.getItemList().clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    String id = snapshot1.getKey();
                    int slm = snapshot1.child("slm").getValue(Integer.class);
                    getItem(id,slm);
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
    private void getItem(String id,int slm){
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_item/"+id);
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Item item = snapshot.getValue(Item.class);
                item.setSlm(slm);
                itemCast.getItemList().add(item);
                castAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
        setVisible(itemStateList);
    }
    private String date(){
        String time = ""; // Khởi tạo biến để lưu trữ ngày giờ
        Date now = new Date(); // Tạo một đối tượng Date đại diện cho ngày giờ hiện tại

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            time = now.toInstant().toString(); // Chuyển đổi đối tượng Date thành Instant và lấy chuỗi biểu diễn của nó
        } else {
            // Xử lý trường hợp phiên bản Android thấp hơn Oreo
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            time = sdf.format(now); // Định dạng ngày giờ và lưu vào biến time
        }
        return time; // Trả về ngày giờ dưới dạng chuỗi
    }
    private void setVisible(List<ItemState> itemStateList){
        if(itemStateList.size() >0 ){
            tvTitle.setVisibility(View.VISIBLE);
            btnBuy.setVisibility(View.VISIBLE);
            tvMoney.setVisibility(View.VISIBLE);
            tvMoney.setText(calculatorMoney(itemStateList)+"k");
        }else{
            btnBuy.setVisibility(View.GONE);
            tvTitle.setVisibility(View.GONE);
            tvMoney.setVisibility(View.GONE);
        }
    }
    private int calculatorMoney(List<ItemState> itemStateList){
        int sum = 0;
        for(int i=0;i<itemStateList.size();i++){
            Item item = itemStateList.get(i).getItem();
            if(item.getFlashSale().isIs()){
                sum+= item.getSlm()*item.getCost()-item.getSlm()*item.getCost()*item.getFlashSale().getPercent()/100;
            }else{
                sum+= item.getSlm()*item.getCost();
            }
        }
        return sum;
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
            reduceSL(itemState.getItem(),itemState.getItem().getSlm());
            increaseSold(itemState.getItem(),itemState.getItem().getSlm());
            deleteItemCast(itemState);
        }
        FragmentManager fragmentManager = requireFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame,new StateFragment()).commit();
    }
    private void deleteItemCast(ItemState itemState){
        DatabaseReference mData = database.getReference("List_cast/"+itemState.getId()+"/"+itemState.getItem().getId());
        mData.removeValue();
    }
    private void reduceSL(Item item,int slBuy){
        String id = item.getId();
        int slc = item.getSL();
        int slm = slc - slBuy;
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_item/"+id);
        mData.child("sl").setValue(slm);
    }
    private void increaseSold(Item item,int slBuy){
        String id = item.getId();
        int soldm = item.getSold()+slBuy;
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_item/"+id);
        mData.child("sold").setValue(soldm);
    }
}