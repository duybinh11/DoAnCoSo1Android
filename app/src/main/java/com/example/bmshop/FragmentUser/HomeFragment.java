package com.example.bmshop.FragmentUser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bmshop.ActivityUser.ItemSelected;
import com.example.bmshop.Adapter.GridAdapter;
import com.example.bmshop.Adapter.ImageSlideAdapter;
import com.example.bmshop.Adapter.NewAdapter;
import com.example.bmshop.Interface.CallBackRecycleview;
import com.example.bmshop.Model.FlashSale;
import com.example.bmshop.Model.Item;
import com.example.bmshop.R;
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
    RecyclerView rccvNew,rccvGrid;
    List<Item> itemListNew,itemListFlash;
    ViewPager2 vp2;
    CircleIndicator3 circleIndicator3;
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
        initImageSlide();
        initItemNew();
        initGrid();
    }

    private void anhXa(View view){
        rccvNew = view.findViewById(R.id.rccvNew);
        rccvGrid = view.findViewById(R.id.rccvGrid);
        itemListNew = new ArrayList<>();
        itemListFlash = new ArrayList<>();
        imageSlideAdapter = new ImageSlideAdapter(itemListFlash);
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
    private void getDataItemFlash(){
        progressDialog.show();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_item");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemListFlash.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Item item = snapshot1.getValue(Item.class);
                    setIsFS(item);
                    if(item.getFlashSale().isIs()){
                        itemListFlash.add(item);
                    }
                }
                imageSlideAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Loi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setIsFS(Item item){
        Date now = new Date();
        Date end = formatDate(item.getFlashSale().getEnd());
        if(now.getTime()-end.getTime()>0){
            DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_item/"+item.getId());
            mData.child("flashSale").child("is").setValue(false);
        }
        Date start = formatDate(item.getFlashSale().getStart());
        if(now.getTime()-start.getTime()<0){
            DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_item/"+item.getId());
            mData.child("flashSale").child("is").setValue(false);
        }
        if(now.getTime() - end.getTime()<0 && now.getTime() - start.getTime()>0){
            DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_item/"+item.getId());
            mData.child("flashSale").child("is").setValue(true);
        }
    }
    private Date formatDate(String time){
        Date date = null;
        String typeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        SimpleDateFormat format = new SimpleDateFormat(typeFormat);
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }
    private void getDataItemNew(){
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Loi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initImageSlide(){
        getDataItemFlash();
        imageSlideAdapter = new ImageSlideAdapter(itemListFlash);
        runnable = new Runnable() {
            @Override
            public void run() {
                if (vp2.getCurrentItem() == itemListFlash.size() - 1){
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
                handler.postDelayed(runnable,4000);
            }
        });
    }
    private void initItemNew(){
        getDataItemNew();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        rccvNew.setLayoutManager(gridLayoutManager);
        rccvNew.setAdapter(newAdapter);
    }
    private void initGrid(){
        GridAdapter gridAdapter = new GridAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        rccvGrid.setAdapter(gridAdapter);
        rccvGrid.setLayoutManager(gridLayoutManager);
    }
}