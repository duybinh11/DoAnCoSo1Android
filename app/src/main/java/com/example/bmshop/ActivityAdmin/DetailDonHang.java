package com.example.bmshop.ActivityAdmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailDonHang extends AppCompatActivity {
    TextView tvNameUser,tvPhone,tvAddress,tvDateBuy,tvSlm,tvMoney;
    ImageView img;
    String status;
    Button btnSave,btnBack,btnDelete;
    ProgressDialog progressDialog;
    List<String> list = new ArrayList<>();
    Spinner spinner;
    ItemState itemState ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_don_hang);
        itemState = (ItemState) getIntent().getSerializableExtra("itemState");
        anhXa();
        initSpinner();
        getDataUser(itemState.getId());
        setDataItem(itemState);
        setVisibleDelete();
        getStatus();
        onCLickSave();
        onClickBack();
    }
    private void anhXa(){
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        img = findViewById(R.id.img);
        spinner = findViewById(R.id.spinner);
        tvNameUser = findViewById(R.id.tvNameUser);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvDateBuy = findViewById(R.id.tvDateBuy);
        tvSlm = findViewById(R.id.tvSlm);
        tvMoney = findViewById(R.id.tvMoney);
        btnDelete = findViewById(R.id.btnDelete);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang Tải");
    }
    private void getDataUser(String id){
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_user/"+id);
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                setDataUser(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailDonHang.this, "Loi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setDataUser(User user){
        tvNameUser.setText(user.getName());
        tvPhone.setText(user.getPhone());
        tvAddress.setText(user.getAddress());
    }
    private void setDataItem(ItemState itemState){
        Glide.with(this).load(itemState.getItem().getImg()).into(img);
        tvDateBuy.setText(date(itemState.getDate()));
        tvSlm.setText(String.valueOf(itemState.getItem().getSlm()));
        tvMoney.setText(itemState.getItem().getCost()*itemState.getItem().getSlm()+"k");
        for (int i = 0; i <list.size() ; i++) {
            if(list.get(i).equals(itemState.getState())){
                spinner.setSelection(i);
            }
        }
    }
    private void initSpinner(){
        list.add("Chuẩn Bị Hàng");
        list.add("Vận Chuyển");
        list.add("Đang Giao");
        list.add("Đã Nhận");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_spinner, list);
        spinner.setAdapter(adapter);
    }
    public void getStatus(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status =parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // không làm gì khi không có giá trị được chọn
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
    private void onClickBack(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void onCLickSave(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveState();
            }
        });
    }
    private void saveState(){
        progressDialog.show();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_state/"+
                itemState.getId()+"/"+itemState.getIdVanChuyen()+"/"+itemState.getItem().getId());
        mData.child("state").setValue(status, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                progressDialog.dismiss();
            }
        });
    }

    private void setVisibleDelete(){
        if(itemState.getState().equals("Đã Nhận")){
            btnDelete.setVisibility(View.VISIBLE);
        }else{
            btnDelete.setVisibility(View.GONE);
        }
    }
}