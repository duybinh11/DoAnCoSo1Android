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
    Button btnBack,btnDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cast);
        anhXa();
        setData(item);
        onClickBack();
        onClickDelete();
    }
    private void anhXa(){
        item = (Item) getIntent().getSerializableExtra("item");
        img = findViewById(R.id.img);
        tvName = findViewById(R.id.tvName);
        tvCost = findViewById(R.id.tvCost);
        tvSl = findViewById(R.id.tvSL);
        tvSold = findViewById(R.id.tvSold);
        tvDate = findViewById(R.id.tvDate);
        tvSlm = findViewById(R.id.count);
        tvMoney = findViewById(R.id.tvMoney);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog  = new ProgressDialog(this);
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
            tvMoney.setText(String.valueOf(item.getSlm()*item.getCost())+"k");
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
}