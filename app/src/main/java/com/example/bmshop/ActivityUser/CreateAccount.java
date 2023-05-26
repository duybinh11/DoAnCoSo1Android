package com.example.bmshop.ActivityUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bmshop.Model.User;
import com.example.bmshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccount extends AppCompatActivity {
    EditText edtEmail,edtPass1,edtName,edtAddress,edtPhone,edtPass2;
    Button btnCreate,btnBack;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        anhXa();
        onClickCreate();
        onClickBack();
    }

    private void anhXa() {
        edtEmail = findViewById(R.id.edtEmail1);
        edtAddress = findViewById(R.id.edtAddress1);
        edtPass1 = findViewById(R.id.edtPass1);
        edtPass2 = findViewById(R.id.edtPass2);
        edtName = findViewById(R.id.edtName1);
        edtPhone = findViewById(R.id.edtPhone1);
        btnCreate = findViewById(R.id.btnCreat);
        btnBack = findViewById(R.id.btnBack);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang Tải");
        database = FirebaseDatabase.getInstance();
    }
    private void onClickCreate(){
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass1 = edtPass1.getText().toString();
                String pass2 =edtPass2.getText().toString();
                if(pass1.equals(pass2)){
                    String email = edtEmail.getText().toString();;
                    String name = edtName.getText().toString();
                    String id = null;
                    String phone = edtPhone.getText().toString();
                    String address = edtAddress.getText().toString();
                    User user = new User(id,name,phone,address);
                    create(email,pass1,user);
                }else{
                    Toast.makeText(CreateAccount.this, "Mật Khẩu Không Khớp", Toast.LENGTH_SHORT).show();
                }
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
    private void create(String email,String password,User userModel){
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String id = user.getUid();
                            userModel.setId(id);
                            writeUser(userModel);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(CreateAccount.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void writeUser(User user){
        DatabaseReference mData = database.getReference("List_user");
        mData.child(user.getId()).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(CreateAccount.this, "Tạo Thành Công", Toast.LENGTH_SHORT).show();
            }
        });
    }


}