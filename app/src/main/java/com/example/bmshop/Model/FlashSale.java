package com.example.bmshop.Model;

import java.io.Serializable;

public class FlashSale implements Serializable {
    private boolean is ;
    private int percent ;
    private String start  ;
    private String end;

    public FlashSale() {
    }

    public FlashSale(boolean isFlashSale, int percent, String start, String end) {
        this.is = isFlashSale;
        this.percent = percent;
        this.start = start;
        this.end = end;
    }

    public boolean isIs() {
        return is;
    }

    public void setIs(boolean is) {
        this.is = is;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "FlashSale{" +
                "is=" + is +
                ", percent=" + percent +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}
