package com.example.bmshop.ActivityUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.bmshop.FragmentUser.CastFragment;
import com.example.bmshop.FragmentUser.StateFragment;
import com.example.bmshop.FragmentUser.HomeFragment;
import com.example.bmshop.FragmentUser.MyFragment;
import com.example.bmshop.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivityUser extends AppCompatActivity {
    ImageView imgSearch;
    BottomNavigationView btnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        anhXa();
        initFragment();
        onClickImg();
    }
    private void anhXa(){
        imgSearch = findViewById(R.id.imgSearch);
        btnv = findViewById(R.id.btnv);
    }
    private void initFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,new HomeFragment()).commit();
        btnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
                    case R.id.Home:{
                        fragmentTransaction.replace(R.id.frame,new HomeFragment()).commit();
                        break;
                    }
                    case R.id.Cast:{
                        fragmentTransaction.replace(R.id.frame,new CastFragment()).commit();
                        break;
                    }
                    case R.id.bought:{
                        fragmentTransaction.replace(R.id.frame,new StateFragment()).commit();
                        break;
                    }
                    case R.id.My:{
                        fragmentTransaction.replace(R.id.frame,new MyFragment()).commit();
                        break;
                    }
                }
                return true;
            }
        });
    }
    private void onClickImg(){
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityUser.this,Search.class);
                startActivity(intent);
            }
        });
    }

}