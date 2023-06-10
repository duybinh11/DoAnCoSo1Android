package com.example.bmshop.Model;

import java.io.Serializable;

public class ItemState implements Serializable {
    private String id;
    private String idVanChuyen;
    private String date;
    private Item item;
    private String state;

    public ItemState() {
    }

    public ItemState(String id, String idVanChuyen, String date, Item item, String state) {
        this.id = id;
        this.idVanChuyen = idVanChuyen;
        this.date = date;
        this.item = item;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIdVanChuyen() {
        return idVanChuyen;
    }

    public void setIdVanChuyen(String idVanChuyen) {
        this.idVanChuyen = idVanChuyen;
    }
}
