package com.example.bmshop.ActivityUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;

import com.example.bmshop.ActivityAdmin.AdminMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bmshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText edtEmail,edtPass;
    FirebaseAuth mAuth;
    TextView tvForget,tvNoAccount;

    ProgressDialog progressDialog;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhXa();
        onClickTvNoAccount();
        onClicktvForget();
        onClickLogin();

    }

    private void anhXa() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);
        tvForget = findViewById(R.id.tvForget);
        tvNoAccount = findViewById(R.id.tvNoAccount);
        btnLogin = findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang Tải");
    }
    private void onClickTvNoAccount() {
        tvNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,CreateAccount.class);
                startActivity(intent);
            }
        });
    }
    private  void onClicktvForget(){
        tvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,ForgetPass.class);
                startActivity(intent);
            }
        });
    }
    private void onClickLogin(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String password = edtPass.getText().toString();
                if(email.equals("") || password.equals("")){
                    Toast.makeText(Login.this, "Vui Lòng Nhập Đầy Đủ Thông Tin", Toast.LENGTH_SHORT).show();
                }else{
                    login(email,password);
                }
            }
        });
    }
    public void login(String email,String password){
        if(email.equals("db") && password.equals("zxc")){
            Intent intent = new Intent(this, AdminMainActivity.class);
            startActivity(intent);
        }else{
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(Login.this,MainActivityUser.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Login.this, "Đăng Nhập Không Thành Công", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }


}