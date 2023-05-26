package com.example.bmshop.ActivityAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.bmshop.Adapter.ViewPager2Adapter;
import com.example.bmshop.FragmentUser.CastFragment;
import com.example.bmshop.FragmentUser.HomeFragment;
import com.example.bmshop.R;
import com.example.bmshop.fragmentAdmin.DonHangFragment;
import com.example.bmshop.fragmentAdmin.ItemFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity {
    ImageView imgPlus;
    TabLayout tab;
    ViewPager2 vp2;

    List<Fragment> fragmentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        anhXa();
        setFragmentList();
        initTablayout();
        onClickImg();
    }
    private void anhXa(){
        imgPlus = findViewById(R.id.imgPlus);
        tab = findViewById(R.id.tab);
        vp2 = findViewById(R.id.vp2);

    }
    private void initTablayout(){
        ViewPager2Adapter adapter = new ViewPager2Adapter(this,fragmentList);
        vp2.setAdapter(adapter);
        new TabLayoutMediator(tab, vp2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0 : {
                        tab.setText("Sản Phẩm");
                        break;
                    }
                    case 1 : {
                        tab.setText("Đơn Hàng");
                        break;
                    }
                }
            }
        }).attach();
    }
    private void setFragmentList(){
        fragmentList = new ArrayList<>();
        fragmentList.add(new ItemFragment());
        fragmentList.add(new DonHangFragment());
    }
    private void onClickImg(){
        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this,AddItem.class);
                startActivity(intent);
            }
        });
    }
}