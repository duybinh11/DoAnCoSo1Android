package com.example.bmshop.ActivityAdmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bmshop.Model.FlashSale;
import com.example.bmshop.Model.Item;
import com.example.bmshop.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AddItem extends AppCompatActivity {
    ImageView img;
    TextView tvGalary;
    EditText edtNameSP,edtSlSP,edtCost;
    Spinner spinner;
    Button btnAdd,btnBack;
    List<String> list;
    String type ;
    Uri uri;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        anhXa();
        onClickGalary();
        onClickAdd();
        onClickBack();
        getType();
    }
    private void anhXa(){
        img = findViewById(R.id.img);
        tvGalary = findViewById(R.id.tvGalary);
        edtNameSP = findViewById(R.id.edtNameSP);
        edtCost = findViewById(R.id.edtCost);
        edtSlSP = findViewById(R.id.edtSLSP);
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang Tải");
        spinner = findViewById(R.id.spinner);
        list = new ArrayList<>();
        initSpinner();
    }
    private void onClickGalary(){
        tvGalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && resultCode == RESULT_OK && data != null){
            uri = data.getData();
            img.setImageURI(uri);
        }
    }
    private void onClickAdd(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtNameSP.getText().toString();
                int sl =Integer.valueOf(edtSlSP.getText().toString());
                int cost =Integer.valueOf(edtCost.getText().toString());
                sentImg(name,sl,cost);
            }
        });
    }
    private void sentImg(String name,int sl,int cost){
        progressDialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("img_item");

        // Tạo tên file ngẫu nhiên
        String filename = UUID.randomUUID().toString();

        // Upload hình ảnh lên Firebase Storage
        StorageReference imageRef = storageRef.child(filename);
        UploadTask uploadTask = imageRef.putFile(uri);

        // Lấy URL của hình ảnh
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return imageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                String url = downloadUri.toString();
                addItem(name,sl,cost,url);
            } else {
                progressDialog.dismiss();
                Toast.makeText(this, "loi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addItem(String name,int sl,int cost,String url){

        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_item");
        String id = mData.push().getKey();
        String time = "";
        Date now = new Date(); // Tạo một đối tượng Date đại diện cho ngày giờ hiện tại

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            time = now.toInstant().toString(); // Chuyển đổi đối tượng Date thành Instant và lấy chuỗi biểu diễn của nó
        } else {
            // Xử lý trường hợp phiên bản Android thấp hơn Oreo
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            time = sdf.format(now); // Định dạng ngày giờ và lưu vào biến time
        }
        FlashSale flashSale = new FlashSale(false ,0,time,time);
        Item item = new Item(id,name,cost,sl,time,0,url,type,flashSale);
        mData.child(id).setValue(item, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                progressDialog.dismiss();
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
    private void initSpinner(){
        list.add("Thời Trang");
        list.add("Skincare");
        list.add("Đồ Ăn");
        list.add("Điện Tử");
        list.add("Gia Dụng");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_spinner, list);
        spinner.setAdapter(adapter);
    }
    public void getType(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type =parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // không làm gì khi không có giá trị được chọn
                type = parent.getItemAtPosition(0).toString();
            }
        });
    }
}