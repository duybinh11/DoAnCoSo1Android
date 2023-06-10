package com.example.bmshop.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bmshop.ActivityUser.ItemSelected;
import com.example.bmshop.Model.Item;
import com.example.bmshop.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> implements Filterable {
    List<Item> itemList;
    private List<Item> filteredList;

    public SearchAdapter(List<Item> itemList){
        this.itemList = itemList;
        this.filteredList = itemList;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reccycviewsearch,parent,false);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        Item item = itemList.get(position);
        Glide.with(holder.itemView.getContext()).load(item.getImg()).into(holder.img);
        holder.tvName.setText(item.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), ItemSelected.class);
                intent.putExtra("item",item);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key = charSequence.toString();
                if(key.isEmpty()){
                    itemList = filteredList;
                }else{
                    List<Item> itemList1 = new ArrayList<>();
                    for (Item item : filteredList){
                        if(item.getName().toLowerCase().contains(key.toLowerCase())){
                            itemList1.add(item);
                        }
                    }
                    itemList = itemList1;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = itemList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemList = (List<Item>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class SearchHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView tvName;
        public SearchHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
