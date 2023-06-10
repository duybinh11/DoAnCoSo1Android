package com.example.bmshop.ActivityAdmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bmshop.Model.FlashSale;
import com.example.bmshop.Model.Item;
import com.example.bmshop.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ItemDetail extends AppCompatActivity {
    ImageView img;
    ProgressDialog progressDialog;
    EditText edtName,edtSL,edtCost;
    EditText edtPercent;
    Button btnDelete,btnSave,btnBack, btnOnFS,btnOffFS;
    TextView tvStart,tvEnd,tvTime,tvTime1,tvPercent;
    Switch aSwitch;
    Item item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        anhXa();
        setData();
        setVisiBleFS();
        setTime();
        onClickBack();
        onClickDelete();
        onClickSave();
        onCLickBtnOnFS();
        onClickBtnOffFS();

    }
    private void anhXa(){

        item = (Item) getIntent().getSerializableExtra("item");

        tvPercent = findViewById(R.id.tvPercent);
        btnOffFS = findViewById(R.id.btnOffFS);
        edtPercent = findViewById(R.id.edtPercent1);
        btnOnFS = findViewById(R.id.btnOnFS);
        tvStart = findViewById(R.id.tvStart);
        tvEnd = findViewById(R.id.tvEnd);
        tvTime = findViewById(R.id.tvTime);
        tvTime1 = findViewById(R.id.tvTime1);
        aSwitch = findViewById(R.id.aswitch);
        img = findViewById(R.id.img);
        edtName = findViewById(R.id.edtName);
        edtCost = findViewById(R.id.edtCost);
        edtSL = findViewById(R.id.edtSL);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
        btnSave = findViewById(R.id.btnSave);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang Tải");
    }
    private void setData(){
        if(item != null){
            Glide.with(this).load(item.getImg()).into(img);
            edtName.setText(item.getName());
            edtSL.setText(String.valueOf(item.getSL()));
            edtCost.setText(String.valueOf(item.getCost()));
        }
        if(item.getFlashSale().isIs()){
            aSwitch.setChecked(true);
            edtPercent.setText(String.valueOf(item.getFlashSale().getPercent()));
            String start = formatDateFB1(item.getFlashSale().getStart());
            String end = formatDateFB1(item.getFlashSale().getEnd());
            tvTime.setText(start);
            tvTime1.setText(end);
            btnOnFS.setVisibility(View.VISIBLE);
            btnOffFS.setVisibility(View.VISIBLE);
        }else{
            btnOnFS.setVisibility(View.GONE);
            btnOffFS.setVisibility(View.GONE);
            edtPercent.setVisibility(View.GONE);
            tvPercent.setVisibility(View.GONE);
            tvTime.setVisibility(View.GONE);
            tvTime1.setVisibility(View.GONE);
            tvStart.setVisibility(View.GONE);
            tvEnd.setVisibility(View.GONE);
        }
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
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_item/"+item.getId());
        mData.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                progressDialog.dismiss();
            }
        });
        deleteImg();
    }
    private void onClickSave(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
            }
        });
    }
    private void saveItem(){
        progressDialog.show();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_item/"+item.getId());
        String name = edtName.getText().toString();
        int sl = Integer.valueOf(edtSL.getText().toString());
        int cost = Integer.valueOf(edtCost.getText().toString());
        Map<String,Object> map = new HashMap<>();
        map.put("/name",name);
        map.put("/sl",sl);
        map.put("/cost",cost);
        mData.updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                progressDialog.dismiss();
            }
        });
    }
    private void deleteImg() {
        // Chuyển đổi đường dẫn URL thành tham chiếu tới tệp trên Firebase Storage
        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(item.getImg());

        // Xóa ảnh
        imageRef.delete();
    }
    private void onClickBtnOffFS(){
        btnOffFS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BtnOffFS();
            }
        });
    }
    private void BtnOffFS(){
        String timeNow = dateNow();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_item/"+item.getId()+"/flashSale");
        mData.child("end").setValue(timeNow, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(ItemDetail.this, "Xong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void onCLickBtnOnFS(){
        btnOnFS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String timeStart = tvTime.getText().toString();
                String timeStartFS = formatDateFB(timeStart);

                String timeEnd = tvTime1.getText().toString();
                String timeEndFS = formatDateFB(timeEnd);

                int percent = Integer.valueOf(edtPercent.getText().toString());


                FlashSale flashSale = new FlashSale(true,percent,timeStartFS,timeEndFS);

                DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_item/"+item.getId()+"/flashSale");
                mData.setValue(flashSale, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(ItemDetail.this, "xong", Toast.LENGTH_SHORT).show();
                }
                });

            }
        });
    }
    private String formatDateFB(String date){
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String time = "";
        try {
            Date date1 = input.parse(date);
            time = output.format(date1).toString();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return time;
    }
    private String dateNow(){
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
    private String formatDateFB1(String date){
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = "";
        try {
            Date date1 = input.parse(date);
            time = output.format(date1).toString();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return time;
    }
    private void setVisiBleFS(){
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    btnOnFS.setVisibility(View.VISIBLE);
                    btnOffFS.setVisibility(View.VISIBLE);
                    edtPercent.setVisibility(View.VISIBLE);
                    tvPercent.setVisibility(View.VISIBLE);
                    tvTime.setVisibility(View.VISIBLE);
                    tvTime1.setVisibility(View.VISIBLE);
                    tvStart.setVisibility(View.VISIBLE);
                    tvEnd.setVisibility(View.VISIBLE);
                }else{
                    btnOnFS.setVisibility(View.GONE);
                    btnOffFS.setVisibility(View.GONE);
                    edtPercent.setVisibility(View.GONE);
                    tvPercent.setVisibility(View.GONE);
                    tvTime.setVisibility(View.GONE);
                    tvTime1.setVisibility(View.GONE);
                    tvStart.setVisibility(View.GONE);
                    tvEnd.setVisibility(View.GONE);
                }
            }
        });
    }
    private void setTime(){
        tvTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ItemDetail.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        calendar.set(selectedYear, selectedMonth, selectedDay);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(ItemDetail.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                                calendar.set(Calendar.SECOND,00);
                                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                                calendar.set(Calendar.MINUTE, selectedMinute);

                                Date selectedDateTime = calendar.getTime();

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String formattedDateTime = dateFormat.format(selectedDateTime);
                                tvTime1.setText(formattedDateTime);
                            }
                        }, hour, minute, false);

                        timePickerDialog.show();
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ItemDetail.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        calendar.set(selectedYear, selectedMonth, selectedDay);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(ItemDetail.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                                calendar.set(Calendar.SECOND,00);
                                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                                calendar.set(Calendar.MINUTE, selectedMinute);

                                Date selectedDateTime = calendar.getTime();

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String formattedDateTime = dateFormat.format(selectedDateTime);
                                tvTime.setText(formattedDateTime);
                            }
                        }, hour, minute, false);

                        timePickerDialog.show();
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });
    }
}