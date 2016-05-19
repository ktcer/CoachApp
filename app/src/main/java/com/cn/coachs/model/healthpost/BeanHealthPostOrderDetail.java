package com.cn.coachs.model.healthpost;

import java.io.Serializable;

public class BeanHealthPostOrderDetail implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;
    private int older;
    private int kids;
    private int normal;
    private String activetime;
    private String cover;
    private String tag;
    private String title;
    private long travelID;

    public int getOlder() {
        return older;
    }

    public void setOlder(int older) {
        this.older = older;
    }

    public int getKids() {
        return kids;
    }

    public void setKids(int kids) {
        this.kids = kids;
    }

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public String getActivetime() {
        return activetime;
    }

    public void setActivetime(String activetime) {
        this.activetime = activetime;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTravelID() {
        return travelID;
    }

    public void setTravelID(long travelID) {
        this.travelID = travelID;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}
