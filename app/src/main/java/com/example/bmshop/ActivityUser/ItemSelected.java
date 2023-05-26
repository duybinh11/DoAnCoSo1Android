package com.example.bmshop.ActivityUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bmshop.Model.Item;
import com.example.bmshop.Model.ItemState;
import com.example.bmshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemSelected extends AppCompatActivity {
    ImageView img;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    TextView tvName,tvCost,tvSl,tvSold,tvDate,tvReduce,tvSlm,tvIncrease;
    Button btnAddCast,btnBuy,btnBack;
    Item item;
    int count = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selected);
        anhXa();
        setData(item);
        SLM();
        onClickAddCast();
        onClickBuy();
    }
    private void anhXa(){
        item = (Item) getIntent().getSerializableExtra("item");
        img = findViewById(R.id.img);
        tvName = findViewById(R.id.tvName);
        tvCost = findViewById(R.id.tvCost);
        tvSl = findViewById(R.id.tvSL);
        tvSold = findViewById(R.id.tvSold);
        tvDate = findViewById(R.id.tvDate);
        tvReduce = findViewById(R.id.reduce);
        tvIncrease = findViewById(R.id.increase);
        tvSlm = findViewById(R.id.count);
        btnAddCast = findViewById(R.id.btnAddCast);
        btnBuy = findViewById(R.id.btnBuy);
        btnBack = findViewById(R.id.btnBack);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang Tải");
    }
    private void setData(Item item){
        if(item != null){
            Glide.with(this).load(item.getImg()).into(img);
            tvName.setText(item.getName());
            tvCost.setText(String.valueOf(item.getCost()));
            tvSl.setText(String.valueOf(item.getSL()));
            tvSold.setText(String.valueOf(item.getSold()));
            String time = date(item.getDate());
            tvDate.setText(time);
        }
    }
    private void SLM(){
        tvReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count >= 2){
                    count--;
                }
                tvSlm.setText(String.valueOf(count));
            }
        });
        tvIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count < item.getSL()){
                    count++;
                }
                tvSlm.setText(String.valueOf(count));
            }
        });
    }
    private void onClickAddCast(){
        btnAddCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCast();
            }
        });
    }
    private void addCast(){
        String idUser = mAuth.getUid();
        int slm = Integer.valueOf(tvSlm.getText().toString());
        item.setSlm(slm);
        progressDialog.show();
        DatabaseReference mData = database.getReference("List_cast/"+idUser+"/"+item.getId());
        mData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int slmCux = snapshot.child("slm").getValue(Integer.class);
                    int slm1Moi = slmCux +slm;
                    Toast.makeText(ItemSelected.this, String.valueOf(slm1Moi), Toast.LENGTH_SHORT).show();
                    mData.child("slm").setValue(slm1Moi, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            progressDialog.dismiss();
                            Toast.makeText(ItemSelected.this, "Thêm Vào Giỏ Thành Công", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    mData.setValue(item, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            progressDialog.dismiss();
                            Toast.makeText(ItemSelected.this, "Thêm Vào Giỏ Thành Công", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(ItemSelected.this, "Thêm Vào Giỏ Thất Bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String date(String date){
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
    private void onClickBuy(){
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyItem();
            }
        });
    }
    private void buyItem(){
        String time = date();
        String idUser = mAuth.getUid();
        int slm = Integer.valueOf(tvSlm.getText().toString());
        item.setSlm(slm);

        DatabaseReference mData = database.getReference("List_state/"+idUser);
        String idVanChuyen = mData.push().getKey();
        ItemState itemState = new ItemState(idUser,idVanChuyen,time,item,"Chuẩn Bị Hàng");
        mData.child(idVanChuyen).child(item.getId()).setValue(itemState, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(ItemSelected.this, "Mua Thành Công", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String date(){
        String time = "";
        Date now = new Date();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            time = now.toInstant().toString();
        }
        return time;
    }

}