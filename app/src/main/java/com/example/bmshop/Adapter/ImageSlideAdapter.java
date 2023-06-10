package com.example.bmshop.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bmshop.ActivityUser.ItemSelected;
import com.example.bmshop.Model.Item;
import com.example.bmshop.R;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ImageSlideAdapter extends RecyclerView.Adapter<ImageSlideAdapter.ImageSlideHolder>{
    List<Item> itemList;
    private CountDownTimer countDownTimer;
    public ImageSlideAdapter( List<Item> itemList){
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ImageSlideHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slide,parent,false);
        return new ImageSlideHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ImageSlideHolder holder, int position) {
        Item item = itemList.get(position);
        Date dateFSEnd = formatDate(item.getFlashSale().getEnd());
        @SuppressLint("ResourceType") Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.drawable.flash);
        holder.tvPercent.startAnimation(animation);
        holder.tvPercent.setText(item.getFlashSale().getPercent()+"%");
        Glide.with(holder.itemView.getContext())
                .load(item.getImg())
                .into(holder.img);
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Lấy thời gian hiện tại
                Date now = new Date();
                long differenceInMillis = dateFSEnd.getTime() - now.getTime();

                long seconds = (differenceInMillis / 1000) % 60;
                long minutes = (differenceInMillis / (1000 * 60)) % 60;
                long hours = (differenceInMillis / (1000 * 60 * 60)) % 24;
                long days = differenceInMillis / (1000 * 60 * 60 * 24);
                hours += days * 24;
                holder.tvTime.setText(seconds+":"+minutes+":"+hours);
            }

            @Override
            public void onFinish() {
                // Xử lý khi CountDownTimer kết thúc (không được gọi trong trường hợp này)
            }
        }.start();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(),ItemSelected.class);
                intent.putExtra("item",item);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ImageSlideHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView tvPercent,tvTime;
        @SuppressLint("ResourceType")
        public ImageSlideHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tvPercent = itemView.findViewById(R.id.tvPercent);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
    private Date formatDate(String time){
        Date date = null;
        String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            date = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
