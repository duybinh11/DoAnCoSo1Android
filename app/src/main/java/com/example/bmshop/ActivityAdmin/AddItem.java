package com.example.bmshop.ActivityAdmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bmshop.Model.Item;
import com.example.bmshop.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.UUID;

public class AddItem extends AppCompatActivity {
    ImageView img;
    TextView tvGalary;
    EditText edtNameSP,edtSlSP,edtCost;
    Button btnAdd,btnBack;
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
        Date now = new Date();
        String time = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            time = now.toInstant().toString();
        }
        Item item = new Item(id,name,cost,sl,time,0,url);
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
}