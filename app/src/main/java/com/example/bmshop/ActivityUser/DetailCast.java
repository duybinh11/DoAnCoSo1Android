package com.example.bmshop.ActivityUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bmshop.Model.Item;
import com.example.bmshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailCast extends AppCompatActivity {
    ImageView img;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    Item item;
    TextView tvName,tvCost,tvSl,tvSold,tvDate,tvSlm,tvMoney;
    TextView tvTitle,tvGiamGia,tvPercent,tvStart,tvStart1,tvEnd,tvEnd1;
    ImageView imgPlus,imgMinus;
    Button btnBack,btnDelete,btnSave;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cast);
        anhXa();
        setData(item);
        setVisibleFS(item);
        onClickChangeSL();
        onClickBack();
        onClickDelete();
        onClickSave(item);
    }
    private void anhXa(){
        item = (Item) getIntent().getSerializableExtra("item");
        img = findViewById(R.id.img);
        imgPlus = findViewById(R.id.imgIncrease);
        imgMinus = findViewById(R.id.imgMinus);
        tvName = findViewById(R.id.tvName);
        tvCost = findViewById(R.id.tvCost);
        tvSl = findViewById(R.id.tvSL);
        tvSold = findViewById(R.id.tvSold);
        tvDate = findViewById(R.id.tvDate);
        tvSlm = findViewById(R.id.tvSlm);
        tvMoney = findViewById(R.id.tvMoney);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
        btnSave = findViewById(R.id.btnSave);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog  = new ProgressDialog(this);
        progressDialog.setMessage("Đang Tải");
        tvTitle = findViewById(R.id.tvTitle);
        tvStart = findViewById(R.id.tvStart);
        tvStart1 = findViewById(R.id.tvStart1);
        tvEnd = findViewById(R.id.tvEnd);
        tvEnd1 = findViewById(R.id.tvEnd1);
        tvGiamGia = findViewById(R.id.tvGiamGia);
        tvPercent = findViewById(R.id.tvPercent);
        count = item.getSlm();
    }
    private void setData(Item item){
        if(item != null){
            tvSlm.setText(count+"");
            Glide.with(this).load(item.getImg()).into(img);
            tvName.setText(item.getName());
            tvCost.setText(String.valueOf(item.getCost()));
            tvSl.setText(String.valueOf(item.getSL()));
            tvSold.setText(String.valueOf(item.getSold()));
            String time = date(item.getDate());
            tvDate.setText(time);
            int money = 0;
            if(item.getFlashSale().isIs()){
                money = item.getCost()*count-item.getCost()*count*item.getFlashSale().getPercent()/100;
            }else{
                money = item.getCost()*count;
            }
            tvMoney.setText(money+"k");
        }
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
    private void onClickBack(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void onClickDelete(){
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem();
            }
        });
    }
    private void deleteItem(){
        progressDialog.show();
        String idUser = mAuth.getUid();
        DatabaseReference mData = database.getReference("List_cast/"+idUser+"/"+item.getId());
        mData.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                progressDialog.dismiss();
                Toast.makeText(DetailCast.this, "Xóa Thành Công", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void onClickChangeSL(){
        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count<item.getSL()){
                    count++;
                    tvSlm.setText(count+"");
                    item.setSlm(count);
                    int money = 0;
                    if(item.getFlashSale().isIs()){
                        money = item.getCost()*count-item.getCost()*count*item.getFlashSale().getPercent()/100;
                    }else{
                        money = item.getCost()*count;
                    }
                    tvMoney.setText(money+"k");
                }
            }
        });
        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count>1){
                    count--;
                    tvSlm.setText(count+"");
                    item.setSlm(count);
                    int money = 0;
                    if(item.getFlashSale().isIs()){
                        money = item.getCost()*count-item.getCost()*count*item.getFlashSale().getPercent()/100;
                    }else{
                        money = item.getCost()*count;
                    }
                    tvMoney.setText(money+"k");
                }

            }
        });
    }
    private void onClickSave(Item item){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSlm(item);
            }
        });
    }
    private void saveSlm(Item item){
        String idUser = mAuth.getUid();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_cast/"+idUser+"/"+item.getId());
        mData.child("slm").setValue(count,new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(DetailCast.this, "Đã Lưu", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private String date1(String date){
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("mm:HH  -  dd/MM/yy");
        String time = "";
        try {
            Date date1 = input.parse(date);
            time = output.format(date1).toString();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return time;
    }

    private void setVisibleFS(Item item){
        if(item.getFlashSale().isIs()){
            String start = date1(item.getFlashSale().getStart());
            String end = date1(item.getFlashSale().getEnd());
            tvTitle.setVisibility(View.VISIBLE);
            tvGiamGia.setVisibility(View.VISIBLE);
            tvPercent.setVisibility(View.VISIBLE);
            @SuppressLint("ResourceType") Animation animation = AnimationUtils.loadAnimation(this, R.drawable.flash);
            tvPercent.startAnimation(animation);
            tvPercent.setText(item.getFlashSale().getPercent()+"%");
            tvStart.setVisibility(View.VISIBLE);
            tvStart1.setVisibility(View.VISIBLE);
            tvStart1.setText(start);
            tvEnd.setVisibility(View.VISIBLE);
            tvEnd1.setVisibility(View.VISIBLE);
            tvEnd1.setText(end);
        }
    }
}