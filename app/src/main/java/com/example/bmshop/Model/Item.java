package com.example.bmshop.Model;

import java.io.Serializable;

public class Item implements Serializable {
    private String id;
    private String name;
    private int cost;
    private int SL;
    String date;
    private int sold;
    private String img;
    private String type;
    private int slm;
    private FlashSale flashSale;

    public Item() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Item(String id, String name, int cost, int SL, String date, int sold, String img, String type) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.SL = SL;
        this.date = date;
        this.sold = sold;
        this.img = img;
        this.type = type;
    }

    public Item(String id, String name, int cost, int SL, String date, int sold, String img, String type,FlashSale flashSale) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.SL = SL;
        this.date = date;
        this.sold = sold;
        this.img = img;
        this.type = type;
        this.flashSale = flashSale;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getSL() {
        return SL;
    }

    public void setSL(int SL) {
        this.SL = SL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getSlm() {
        return slm;
    }

    public void setSlm(int slm) {
        this.slm = slm;
    }

    public FlashSale getFlashSale() {
        return flashSale;
    }

    public void setFlashSale(FlashSale flashSale) {
        this.flashSale = flashSale;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", SL=" + SL +
                ", date='" + date + '\'' +
                ", sold=" + sold +
                ", img='" + img + '\'' +
                ", type='" + type + '\'' +
                ", slm=" + slm +
                ", flashSale=" + flashSale +
                '}';
    }
}
