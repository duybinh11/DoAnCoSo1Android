package com.example.bmshop.FragmentUser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bmshop.ActivityUser.ItemSelected;
import com.example.bmshop.Adapter.BanChayAdapter;
import com.example.bmshop.Adapter.ImageSlideAdapter;
import com.example.bmshop.Adapter.NewAdapter;
import com.example.bmshop.Interface.CallBackRecycleview;
import com.example.bmshop.Model.Item;
import com.example.bmshop.R;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class HomeFragment extends Fragment {
    RecyclerView rccvBanChay,rccvNew;
    List<Item> itemListNew;
    ViewPager2 vp2;
    CircleIndicator3 circleIndicator3;
    List<Item> itemListBanChay;
    BanChayAdapter banChayAdapter;
    ImageSlideAdapter imageSlideAdapter;
    NewAdapter newAdapter;
    ProgressDialog progressDialog;
    Handler handler;
    Runnable runnable;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anhXa(view);
        initBanChay();
        initImageSlide();
        initItemNew();
    }

    private void anhXa(View view){
        rccvBanChay = view.findViewById(R.id.rccvBanChay);
        rccvNew = view.findViewById(R.id.rccvNew);
        itemListBanChay = new ArrayList<>();
        itemListNew = new ArrayList<>();
        banChayAdapter = new BanChayAdapter(itemListBanChay);
        newAdapter = new NewAdapter(itemListNew, new CallBackRecycleview() {
            @Override
            public void setDate(Item item) {
                Intent intent = new Intent(getContext(), ItemSelected.class);
                intent.putExtra("item",item);
                startActivity(intent);
            }
        });
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang Tải");
        vp2 = view.findViewById(R.id.vp2);
        circleIndicator3 = view.findViewById(R.id.indicator3);

        handler = new Handler();
    }
    private void initBanChay(){
        setDataBanChay();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        rccvBanChay.setAdapter(banChayAdapter);
        rccvBanChay.setLayoutManager(linearLayoutManager);
    }
    private void setDataBanChay(){
        progressDialog.show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("List_item");
        Query query = ref.orderByChild("sold").limitToFirst(5);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemListBanChay.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Item itemNew = postSnapshot.getValue(Item.class);
                    itemListBanChay.add(0,itemNew);
                }
                banChayAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "loi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setDataItemNew(){
        progressDialog.show();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_item");
        Query query = mData.orderByChild("date");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemListNew.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Item item = snapshot1.getValue(Item.class);
                    itemListNew.add(0,item);
                }
                progressDialog.dismiss();
                newAdapter.notifyDataSetChanged();
                imageSlideAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Loi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initImageSlide(){
        imageSlideAdapter = new ImageSlideAdapter(itemListNew);
        runnable = new Runnable() {
            @Override
            public void run() {
                if (vp2.getCurrentItem() == itemListBanChay.size() - 1){
                    vp2.setCurrentItem(0);
                }else{
                    vp2.setCurrentItem(vp2.getCurrentItem()+1);
                }
            }
        };
        vp2.setAdapter(imageSlideAdapter);
        circleIndicator3.setViewPager(vp2);
        imageSlideAdapter.registerAdapterDataObserver(circleIndicator3.getAdapterDataObserver());
        vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,2000);
            }
        });
    }
    private void initItemNew(){
        setDataItemNew();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        rccvNew.setLayoutManager(gridLayoutManager);
        rccvNew.setAdapter(newAdapter);
    }
}