package com.cn.coachs.ui.patient.others.myaccount.performance;

public class Statistic {
    private String item;
    private String numberOfItem;

    public Statistic(String item, String numberOfItem) {
        this.item = item;
        this.numberOfItem = numberOfItem;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getNumberOfItem() {
        return numberOfItem;
    }

    public void setNumberOfItem(String numberOfItem) {
        this.numberOfItem = numberOfItem;
    }

}
