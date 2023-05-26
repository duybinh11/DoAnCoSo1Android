package com.example.bmshop.Model;

import java.util.List;

public class ItemCast {
    private String id;
    private List<Item> itemList;
    public ItemCast() {
    }

    public ItemCast(String id, List<Item> itemList) {
        this.id = id;
        this.itemList = itemList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
