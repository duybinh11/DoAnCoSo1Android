package com.example.bmshop.ActivityUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bmshop.Model.Item;
import com.example.bmshop.Model.ItemState;
import com.example.bmshop.Model.User;
import com.example.bmshop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailState extends AppCompatActivity {
    FirebaseDatabase database;
    ImageView img;
    TextView tvName,tvCost,tvSL,tvSold,tvDate,tvNameUser,tvAddress,tvPhone,tvDateBuy;
    Button btnHuy,btnBack,btnDaNhan;
    ItemState itemState;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_state);
        anhXa();
        setDataItem(itemState);
        getUser(itemState.getId());
        onClickHuy();
        onClickBack();
        setVisibleBtn(itemState);
        onClickDaNhan();
    }
    private void anhXa(){
        img = findViewById(R.id.img);
        tvName = findViewById(R.id.tvName);
        tvCost = findViewById(R.id.tvCost);
        tvSL = findViewById(R.id.tvSL);
        tvDate = findViewById(R.id.tvDate);
        tvSold = findViewById(R.id.tvSold);
        tvNameUser = findViewById(R.id.tvNameUser);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvDateBuy = findViewById(R.id.tvDateBuy);
        btnBack = findViewById(R.id.btnBack);
        btnHuy = findViewById(R.id.btnHuy);
        btnDaNhan = findViewById(R.id.btnDaNhan);
        itemState = (ItemState) getIntent().getSerializableExtra("itemState");
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang Tải");
    }
    private void setDataItem(ItemState itemState1){
        if(itemState1 != null){
            Item item = itemState1.getItem();
            Glide.with(this).load(item.getImg()).into(img);
            tvName.setText(item.getName());
            tvCost.setText(String.valueOf(item.getCost()));
            tvSL.setText(String.valueOf(item.getSL()));
            String time = formatDate(item.getDate());
            tvDate.setText(time);
            tvSold.setText(String.valueOf(item.getSold()));
            String time1 = formatDate(itemState1.getDate());
            tvDateBuy.setText(time1);
        }
    }
    private void setDateUser(User user){
        if(user != null){
            tvNameUser.setText(user.getName());
            tvAddress.setText(user.getAddress());
            tvPhone.setText(user.getPhone());
        }
    }
    private void getUser(String idUser){
        DatabaseReference mData = database.getReference("List_user/"+idUser);
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               User user = snapshot.getValue(User.class);
               setDateUser(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailState.this, "loi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String formatDate(String date){
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        String time = "";
        try {
            Date date1 = input.parse(date);
            time = output.format(date1).toString();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return time;
    }
    private void onClickBack(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void onClickHuy(){
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HuyItem(itemState);
            }
        });
    }
    private void HuyItem(ItemState itemState){
        if(itemState.getState().equals("Chuẩn Bị Hàng")){
            progressDialog.show();
            DatabaseReference mData = database.getReference("List_state/"+
                    itemState.getId()+"/"+itemState.getIdVanChuyen()+"/"+itemState.getItem().getId());
            mData.removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    progressDialog.dismiss();
                    Toast.makeText(DetailState.this, "Đã Hủy Đơn Hàng Thành Công", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "Không Thể Huy", Toast.LENGTH_SHORT).show();
        }
    }
    private void setVisibleBtn(ItemState itemState){
        if(itemState.getState().equals("Đang Đến Bạn")){
            btnDaNhan.setVisibility(View.VISIBLE);
        }
    }
    private void onClickDaNhan(){
        btnDaNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DaNhanItem(itemState);
            }
        });
    }
    private void DaNhanItem(ItemState itemState){
        itemState.setState("Đã Nhận Hàng");
        progressDialog.show();
        DatabaseReference mData = database.getReference("List_da_mua/"+itemState.getId()+"/"
                +itemState.getIdVanChuyen()+itemState.getItem().getId());
        mData.setValue(itemState, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                progressDialog.dismiss();
                Toast.makeText(DetailState.this, "Cảm Ơn Bạn <3", Toast.LENGTH_SHORT).show();
            }
        });
    }
}