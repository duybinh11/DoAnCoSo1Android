package com.example.bmshop.FragmentUser;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bmshop.Adapter.StateAdapter;
import com.example.bmshop.Model.Item;
import com.example.bmshop.Model.ItemState;
import com.example.bmshop.Model.User;
import com.example.bmshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyFragment extends Fragment {
    ProgressDialog progressDialog;
    TextView tvEmail,tvName,tvPhone,tvAdress;
    RecyclerView rccvDaBan;
    FirebaseAuth mAuth =FirebaseAuth.getInstance();
    String idUser = mAuth.getUid();
    List<ItemState> itemStateList;
    StateAdapter stateAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anhXa(view);
        getDataUser(idUser);
        getListItem(idUser);
        initAdapter();
    }
    private void anhXa(View view){
        tvName = view.findViewById(R.id.tvName);
        tvAdress = view.findViewById(R.id.tvAddress);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvEmail = view.findViewById(R.id.tvEmail);
        rccvDaBan = view.findViewById(R.id.rccvDaBan);
        progressDialog = new ProgressDialog(getContext());
        itemStateList = new ArrayList<>();
        stateAdapter = new StateAdapter(itemStateList);
        progressDialog.setMessage("Đang Tải");
    }
    private void getDataUser(String idUser){
        progressDialog.show();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_user/"+idUser);
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                setDataProfile(user,mAuth.getCurrentUser().getEmail());
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "loi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setDataProfile(User user , String email){
        tvName.setText(user.getName());
        tvPhone.setText(user.getPhone());
        tvEmail.setText(email);
        tvAdress.setText(user.getAddress());
    }
    private void getListItem(String idUser){
        progressDialog.show();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("List_da_mua/"+idUser);
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemStateList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    ItemState itemState = snapshot1.getValue(ItemState.class);
                    itemStateList.add(itemState);
                }
                progressDialog.dismiss();
                stateAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "loi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initAdapter(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rccvDaBan.setAdapter(stateAdapter);
        rccvDaBan.setLayoutManager(linearLayoutManager);
    }




}