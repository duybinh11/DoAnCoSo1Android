package com.example.bmshop.ActivityUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Build;
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
import java.util.Locale;

public class ItemSelected extends AppCompatActivity {
    ImageView img;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    TextView tvName,tvCost,tvSl,tvSold,tvDate,tvReduce,tvSlm,tvIncrease,tvType,tvMoney;
    TextView tvTitle,tvGiamGia,tvPercent,tvStart,tvStart1,tvEnd,tvEnd1;
    Button btnAddCast,btnBuy,btnBack;
    Item item;
    int count = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selected);
        anhXa();
        setData(item);
        setVisibleFS(item);
        SLM();
        onClickAddCast();
        onClickBuy();
        onClickBack();
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
        tvGiamGia = findViewById(R.id.tvGiamGia);
        tvPercent = findViewById(R.id.tvPercent);
        btnAddCast = findViewById(R.id.btnAddCast);
        btnBuy = findViewById(R.id.btnBuy);
        tvMoney = findViewById(R.id.tvMoney);
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvTitle);
        tvStart = findViewById(R.id.tvStart);
        tvStart1 = findViewById(R.id.tvStart1);
        tvEnd = findViewById(R.id.tvEnd);
        tvEnd1 = findViewById(R.id.tvEnd1);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang Tải");
        tvType = findViewById(R.id.tvType);
    }
    private void setData(Item item){
        if(item != null){
            Glide.with(this).load(item.getImg()).into(img);
            tvName.setText(item.getName());
            tvCost.setText(String.valueOf(item.getCost())+"k");
            tvSl.setText(String.valueOf(item.getSL()));
            tvSold.setText(String.valueOf(item.getSold()));
            String time = date(item.getDate());
            tvDate.setText(time);
            if(item.getType() == null){
                tvType.setText("K Xác Định");
            }else{
                tvType.setText(item.getType());
            }
            if(item.getFlashSale().isIs()){
                int money = count*item.getCost()-count*item.getCost()*item.getFlashSale().getPercent()/100;
                tvMoney.setText(money+"k");
            }else{
                int money = count*item.getCost();
                tvMoney.setText(money+"k");
            }
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
                if(item.getFlashSale().isIs()){
                    int money = count*item.getCost()-count*item.getCost()*item.getFlashSale().getPercent()/100;
                    tvMoney.setText(money+"k");
                }else{
                    int money = count*item.getCost();
                    tvMoney.setText(money+"k");
                }
            }
        });
        tvIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count < item.getSL()){
                    count++;
                }
                tvSlm.setText(String.valueOf(count));
                if(item.getFlashSale().isIs()){
                    int money = count*item.getCost()-count*item.getCost()*item.getFlashSale().getPercent()/100;
                    tvMoney.setText(money+"k");
                }else{
                    int money = count*item.getCost();
                    tvMoney.setText(money+"k");
                }
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
        progressDialog.show();
        DatabaseReference mData = database.getReference("List_cast/"+idUser+"/"+item.getId());
        mData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int slmCux = snapshot.child("slm").getValue(Integer.class);
                    int slm1Moi = slmCux +slm;
                    mData.child("slm").setValue(slm1Moi, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            progressDialog.dismiss();
                            Toast.makeText(ItemSelected.this, "Thêm Vào Giỏ Thành Công", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    mData.child("slm").setValue(slm, new DatabaseReference.CompletionListener() {
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
    private void onClickBuy(){
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyItem();
            }
        });
    }
    private void onClickBack(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                Toast.makeText(ItemSelected.this,"Mua Thành Công", Toast.LENGTH_SHORT).show();
            }
        });
        reduceSL(item,slm);
        increaseSold(item,slm);
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